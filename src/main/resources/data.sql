
INSERT  into  Papel (id,autoridade) VALUES ( 1,'usuario') ON CONFLICT (id) DO NOTHING;

INSERT  into  Papel (id,autoridade) VALUES ( 2,'proprietario') ON CONFLICT (id) DO NOTHING;

INSERT  into  papel (id,autoridade) VALUES ( 3,'administrador') ON CONFLICT (id) DO NOTHING;