# awbd-microservices
## A.	 Arhitectura aplicatiei

Aplicatia reprezinta o platforma prin care se pot realiza programari la o spalatorie auto si contine 7 servicii care comunica intre ele.

### 1.	Config-server: 
-	Un serviciu care ajuta la actualizarea proprietatilor in timpul functionarii serviciilor schedule, eureka-namingserver, job-service, appointment-service, employee-service, car-service;
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
-	Proprietatile serviciului pot fi actualizate automat atunci cand sunt modificate in repository-ul de github, prin apelarea urmatorului endpoint: http://localhost:8084/actuator/refresh;
-	Request-urile facute catre acest serviciu pot fi urmarite din Zipkin: http://localhost:9411

### 4.	Car-service:
-	Aplicatie de tip REST;
-	Se pot adauga, modifica, sterge masini, se pot vedea toate masinile sau doar o anumita masina, in functie de id;
-	Proprietatile serviciului pot fi actualizate automat atunci cand sunt modificate in repository-ul de github, prin apelarea urmatorului endpoint: http://localhost:8082/actuator/refresh;


### 5.	Employee-service:
-	Aplicatie de tip REST;
-	Se pot adauga, modifica, sterge angajati, se pot vedea toti angajatii sau doar un anumit angajat, in functie de id;
-	Proprietatile serviciului pot fi actualizate automat atunci cand sunt modificate in repository-ul de github, prin apelarea urmatorului endpoint: http://localhost:8083/actuator/refresh;


### 6.	Job-service:
-	Aplicatie de tip REST;
-	Se pot adauga, modifica, sterge job-uri, se pot vedea toate job-urile sau doar un anumit job dupa id;
-	Proprietatile serviciului pot fi actualizate automat atunci cand sunt modificate in repository-ul de github, prin apelarea urmatorului endpoint: http://localhost:8080/actuator/refresh;


### 7.	Appointment-service:
-	Aplicatie de tip REST;
-	Se pot adauga, modifica, sterge programari, se pot vedea toate programarile sau doar o anumita programare in functie de id;
-	Proprietatile serviciului pot fi actualizate automat atunci cand sunt modificate in repository-ul de github, prin apelarea urmatorului endpoint: http://localhost:8181/actuator/refresh;
-	Aplicatia apeleaza serviciul schedule folosindu-se de FEIGN-proxy;

## B.	Scenarii de utilizare

### 1.	Pornirea serviciilor
Pentru fiecare serviciu trebuie facuti urmatorii pasi:
-	Ruleaza mvn clean install;
-	Creeaza imaginea de docker utilizand Dockerfile-ul prezent in radacina proiectului folosind comanda docker build -t <nume_imagine> . ;
-   Porneste containerele de docker utilizand docker-compose-ul prezent in folderul awbd-microservices si ruland comanda docker-compose up -d;

### 2.	Testarea functionalitatilor din car-service, job-service, employee-service, appointment-service
Pentru a testa endpoint-urile serviciilor car-service, job-service, employee-service, appointment-service se pot accesa link-urile acestea: http://localhost:8082/swagger-ui.html, http://localhost:8080/swagger-ui.html, http://localhost:8083/swagger-ui.html, http://localhost:8181/swagger-ui.html. 
