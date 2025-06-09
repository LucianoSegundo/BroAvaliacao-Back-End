package com.LFSoftware.BroAvaliacao.Serviços;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.CadastroRequest;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.LoginRequest;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.LoginResponse;
import com.LFSoftware.BroAvaliacao.Entidade.Configuracao;
import com.LFSoftware.BroAvaliacao.Entidade.Papel;
import com.LFSoftware.BroAvaliacao.Entidade.Usuario;
import com.LFSoftware.BroAvaliacao.Exceções.AcessoNegadoException;
import com.LFSoftware.BroAvaliacao.Exceções.AusendiaDadosException;
import com.LFSoftware.BroAvaliacao.Exceções.CadastroNedadoException;
import com.LFSoftware.BroAvaliacao.Exceções.EntidadeNaoEncontradaException;
import com.LFSoftware.BroAvaliacao.Repositorio.PapelRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.UsuarioRepository;

@Service
public class UsuarioService {

	private UsuarioRepository userRepo;
	private PapelRepository papelRepo;
	private BCryptPasswordEncoder codSenha; // codifica a senha;
	private JwtEncoder jwtCodificador; // quem gera o token

	public UsuarioService(UsuarioRepository userRepo, PapelRepository papelRepo, BCryptPasswordEncoder codificadorSenha,
			JwtEncoder codificador) {
		this.userRepo = userRepo;
		this.papelRepo = papelRepo;
		this.codSenha = codificadorSenha;
		this.jwtCodificador = codificador;
	}

	@Transactional()
	public LoginResponse Login(LoginRequest request) {
		System.out.println("começo login");
		if (request.senha().isBlank() || request.usuario().isBlank())
			throw new AcessoNegadoException("Nenhum campo da requisição de login pode estar vazio");

		Usuario consulta = userRepo.findByUsuario(request.usuario())
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não foi encontrado"));

		if (codSenha.matches(request.senha(), consulta.getSenha())) {
			System.out.println("tudo ocorreu direitinho no Login");
			return gerarToken(consulta);

		}
		else {
			throw new AcessoNegadoException("Não foi possivelfazer login.");
		}
	}

	@Transactional()
	public LoginResponse cadastroUsuario(CadastroRequest cadRequest) {

		System.out.println("começo cadastro");

		if (cadRequest.email() == null || cadRequest.senha() == null || cadRequest.usuario() == null)
			throw new CadastroNedadoException("Nenhum campo das informações do usuário pode estar em branco");

		if (cadRequest.email().isBlank() || cadRequest.senha().isBlank() || cadRequest.usuario().isBlank())
			throw new CadastroNedadoException("Nenhum campo das informações do usuário pode estar em branco");

		if (userRepo.existsByUsuario(cadRequest.usuario()))
			throw new CadastroNedadoException("Usuario já cadastrado");

		if (userRepo.existsByEmail(cadRequest.email()))
			throw new CadastroNedadoException("Email já em uso.");

		// execução da criação

		Usuario user = new Usuario();

		String senhaCodificada = codSenha.encode(cadRequest.senha());

		Papel papel = papelRepo.findByAutoridade("usuario")
				.orElseThrow(() -> new AusendiaDadosException("houve um problema interno"));

		papel.addUsuario(user);

		user.setUsuario(cadRequest.usuario());
		user.setEmail(cadRequest.email());
		user.setSenha(senhaCodificada);
		user.getPapeis().add(papel);
		user.setConfiguracoes(new Configuracao());

		Usuario usuarioSalvo = userRepo.save(user);

		System.out.println("tudo ocorreu direitinho no cadastro");
		return gerarToken(usuarioSalvo);

	}

	public LoginResponse gerarToken(Usuario usuario) {

		Instant now = Instant.now();
		Long expiresIn = 300L;

		var scopes = usuario.getPapeis().stream().map(papel -> papel.toString()).collect(Collectors.joining(" "));

		var claims = JwtClaimsSet.builder().issuer("ProjetoCMA").subject(usuario.getId().toString())
				.expiresAt(now.plusSeconds(expiresIn)).claim("scope", scopes).build();

		var jwt = jwtCodificador.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		return new LoginResponse(jwt, expiresIn);

	}

}
