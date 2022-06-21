# awbd-microservices
## A.	 Arhitectura aplicatiei

Aplicatia reprezinta o platforma prin care se pot realiza programari la o spalatorie auto si contine 4 servicii care comunica intre ele.

### 1.	Config-server: 
-	Un serviciu care ajuta la actualizarea proprietatilor in timpul functionarii serviciilor schedule si eureka-namingserver;
-	Configuratiile sunt stocate intr-un repository de github: https://github.com/vladdimisca/awbd-config-files ;
-	Proprietatile pot fi injectate din repository-ul de mai sus fara a se face rebuild la imaginile de docker, oricand acele proprietati sunt modificate. Serviciile pot lua proprietatile fara a restarta toate instantele create. Se pot face diferite versiuni de proprietati, de exemplu: dev, prod etc;
-	Se foloseste adnotarea @EnableConfigServer pe clasa ConfigServerApplication;

### 2.	Eureka-namingserver:
-	Un serviciu de service discovery care este folosit pentru inregistrarea instantelor microserviciilor create;
-	Se foloseste de adnotarea @EnableEurekaServer pe clasa EurekaNamingserverApplication;
-	Proprietatile serviciului pot fi actualizate automat atunci cand sunt modificate in repository-ul de github, prin apelarea urmatorului endpoint: http://localhost:8071/actuator/refresh;
-	Request-urile facute catre acest serviciu pot fi urmarite din Zipkin: http://localhost:9411

### 3.	Schedule:
-	Un serviciu care contine orele de functionare ale spalatoriei auto in application.properties;
-	Acest serviciu este rulat in 3 instante;
-	Proprietatile serviciului pot fi actualizate automat atunci cand sunt modificate in repository-ul de github, prin apelarea urmatorului endpoint: http://localhost:8082/actuator/refresh;
-	Request-urile facute catre acest serviciu pot fi urmarite din Zipkin: http://localhost:9411

### 4.	Serviciul principal, Car wash:
-	Aplicatie de tip REST ce contine doua tipuri de utilizatori: Admin si Guest.
-	Utilizatorii se pot inregistra si vor avea rolul de Guest.
-	Utilizatorii Guest isi pot modifica informatiile personale si isi pot sterge contul, pot adauga masini, pot modifica, sterge si vedea masinile proprii, pot crea programari, ulterior avand dreptul de a-si modifica, vedea sau sterge propriile programari si pot vedea job-urile disponibile;
-	Cand utilizatorii incearca sa creeze programari se apeleaza serviciul schedule pentru a se consulta orarul;
-	Administratorii pot adauga, vedea, modifica si sterge angajati si job-uri (servicii ale angajatilor), pot vedea toti utilizatorii, toate masinile acestora si toate appointment-urile;
-	Aplicatia apeleaza serviciul schedule folosindu-se de FEIGN-proxy;

## B.	Scenarii de utilizare

### 1.	Pornirea serviciilor
Pentru fiecare din cele 4 aplicatii trebuie facuti urmatorii pasi:
-	Ruleaza mvn clean install;
-	Creeaza imaginea de docker utilizand Dockerfile-ul prezent in radacina proiectului folosind comanda docker build -t <nume_imagine> . ;
Porneste containerele de docker utilizand docker-compose-ul prezent in folderul awbd-microservices si ruland comanda docker-compose up -d;

### 2.	Testarea functionalitatilor din Car wash
Pentru a testa endpoint-urile aplicatiei Car wash se poate accesa link-ul acesta: http://localhost:8080/swagger-ui.html si pentru a se putea accesa endpoint-urile securizate este nevoie de un token valid adaugat de la butonul Authorize din dreapta sus paginii. Acest token se poate obtine prin logare din Postman folosindu-se urmatorul url: http://localhost:8080/users/login, iar ca body, pentru un cont de Admin se poate folosi:  
{  
    "email":"admin@awbd.com",  
    "password":"12345"  
}  
iar pentru un cont de Guest se poate folosi:  
{  
    "email":"guest@awbd.com",  
    "password":"12345"  
}  
Token-ul poate fi copiat din headerul Authorization dupa apelarea acestui endpoint.  
De asemenea, se pot consulta informatii despre endpoint-uri apelandu-se din Postman urmatorul url: http://localhost:8080/v3/api-docs.  

