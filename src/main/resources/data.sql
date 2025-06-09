
INSERT  into  Papel (id,autoridade) VALUES ( 1,'aluno') ON CONFLICT (id) DO NOTHING;

INSERT  into  Papel (id,autoridade) VALUES ( 2,'professor') ON CONFLICT (id) DO NOTHING;

INSERT  into  papel (id,autoridade) VALUES ( 3,'administrador') ON CONFLICT (id) DO NOTHING;