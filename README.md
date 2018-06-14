# Lekarna - Projektna naloga pri predmetu Praktikum 2 


## Navodila za vzpostavitev projekta:
* Prenesi vsebino repozitorija

* Importaj projekt Lekarna

* Kopiraj *application-roles.properties* in *application-users.properties* datoteki v "Wildfly 12. root/domain/configuration"

* Kopiraj template.xsl v "Wildfly 12. root/standalone/data"

* Preko čarovnika v Wildfly administracijski konzoli (privzeto: localhost:9990) gremo pod deployments-->add-->next-->izberemo lekarna.war datoteko-->next

* Dostopamo do začetne strani preko localhost:8080/Lekarna/domov.xhtml

### Predzahteve
* Naložena vsaj Java 8

* Wildfly 12

* MySQL povezava

### Podatkovna baza
* odzipaj podatkovnabaza.zip 

* ustvari shemo lekarna

* importaj v mysql workbench ali zaženi .sql datoteke znotraj programa, ki podpira skripte (server -> data import)

* datasource povezava : java:jboss/datasource/lekarna


## Definirane vloge:
* Zdravnik

* Lekarnar


## Shema trenutne baze
![alt text](https://github.com/mesner1/Praktikum/blob/master/PodatkovnaBaza.png)


 
