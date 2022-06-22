# ISA_MRS_TEAM05_2022

Projekat iz Internet softverske arhitekture i Metodologija razvoja softvera na FTN-u, 2022.

## Informacije:
Broj tima: 5 <br/>
Grupa:     2 <br/>
Tema projekta: Aplikacija za rezevaciju vikendica, brodova i instruktora pecanja. <br/>

## Pokretanje projekta:

### Deploy:
Link za deploy backend dela aplikacije: https://isa-mrs-tim5-back.herokuapp.com/ <br/>
Link za deploy frontend dela aplikacije: https://isa-mrs-tim-o5-front.herokuapp.com/

Rest servise je moguće direktno pozivati, npr https://isa-mrs-tim-05-back.herokuapp.com/bookingApp/places <br/>

### Lokalno:

#### - Pokretanje backend dela aplikacije
Nakon kloniranja repozitorijuma, potrebno je pozicionirati se u <b>BookingAppTeam05</b> i pokrenuti <b>pom.xml</b> ili kroz maven ili importom u neko od razvojnih okruženja, npr. IntelliJ.<br/><br/>
U <b>BookingAppTeam05\src\main\resources</b> se nalazi <b>application.properties</b> fajl u kojem je potrebno podesiti konfiguracione parametre za PotsgreSQL bazu.<br/>
Podesiti: spring.datasource.url, spring.datasource.username i spring.datasource.password

#### - Pokretanje frontend dela aplikacije
Pozicionirati se u <b>react-app</b> folder. Potrebno je imati node i npm instaliran.
<br>
<br>
Pre samog pokretanja, potrebno je instalirati sve potrebne pakete koji se nalaze u package.json fajlu. Ukoliko dođe do problema sa verzijom package managera odraditi komandu npm update --force.
<br/>
<br/>
Nakon uspešne instalacije svih paketa pokrenuti aplikaciju sa npm install. Ukoliko sve prođe kako treba aplikacija bi trebala biti pokrenuta na localhost:3000


