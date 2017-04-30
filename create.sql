
CREATE TABLE userapp
(
  id serial NOT NULL,
  followersgithuburl character varying(255),
  githubaccount character varying(255),
  linkedinaccount character varying(255),
  orgsgithuburl character varying(255),
  rank double precision,
  reposgithuburl character varying(255),
  CONSTRAINT userapp_pkey PRIMARY KEY (id),
  CONSTRAINT userapp_githubaccount_key UNIQUE (githubaccount),
  CONSTRAINT userapp_linkedinaccount_key UNIQUE (linkedinaccount)
);

CREATE TABLE githubrepository
(
  id serial NOT NULL,
  language character varying(255),
  languages_url character varying(255),
  name character varying(255),
  userapp_id bigint,
  CONSTRAINT githubrepository_pkey PRIMARY KEY (id),
  CONSTRAINT fk_githubrepository_userapp_id FOREIGN KEY (userapp_id)
      REFERENCES userapp (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE githubrepository_languages
(
  githubrepository_id bigint,
  languages character varying(255),
  CONSTRAINT fk_githubrepository_languages_githubrepository_id FOREIGN KEY (githubrepository_id)
      REFERENCES githubrepository (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE OR REPLACE VIEW ranking AS
SELECT row_number() OVER (ORDER BY userapp.rank DESC) AS rankingposition,
userapp.id AS iduser,
userapp.rank AS ranking
FROM userapp;
