# Λέοναρντ Πέπα ics20033 Εργαστήριο 1

# Σχεδίαση Κατανεμημένου Συστήματος Τραπεζικών Λειτουργιών (ΑΤΜ)

# Αρχιτεκτονική Τριων Επιπέδων (3-Tier)

## Eπίπεδο Παρουσίασης

Eίναι υπεύθυνο για την παρουσίαση των δεδομένων και την αλληλεπίδραση του χρήστη
με το σύστημα, αυτό συνήθως επιτυγχάνεται με την χρήση γραφικής διασύνδεσης όπως
είναι μία ιστοσελίδα τράπεζας, μία εφαρμογή web banking στο κινητό ή
και το παραδοσιακό μηχάνημα ATM έξω απο μία
τράπεζα που σου παρέχει όλες τις επιλογές σε μια οθόνη.

## Επίπεδο Επιχειρηματικής Λογικής
Είναι υπεύθυνο για όλη την λειτουργικότητα του συστήματος και την σύνδεση
με την βάση δέδομενων. Δέχεται αιτήματα απο τους χρήστες 
εκτελεί τους απαραίτητους ελέγχους, επεξεργάζεται τα αίτηματα και ανταποκρίνεται
με το κατάλληλο μήνυμα για κάθε αίτημα. Στην περιπτώση μας θα μπορούσε να ελέγχει την αυθεντικοποίηση του χρήστη όταν θέλει
να πραγματοποιήσει σύνδεση στο σύστημα ή αν διαθέτει επαρκή χρηματικό ποσό στον λογαριασμό
του για την πραγματοποίηση ανάληψης κτλπ. Η υλποίηση του συγκεκριμένου επιπέδου γίνεται
αναπτύσοντας μια διεπαφή προγραμμτισμού εφαρμογών (API) σε μία γλώσσα προγραμμτισμού, που θα προσφέρει
κάποιες υπηρεσίες όπως ειναι πχ η ανάληψη και η κατάθεση οι οποίες θα καταναλώνονται απο το επίπεδο παρουσίασης
που είδαμε παραπάνω. Ένα παράδειγμα μιας διεπαφης θα μπορούσε να είναι ένα REST API που η επικοινωνία γίνεται με JSON
αντικείμενα με το πρωτόκολλο HTTP.

## Επίπεδο Δεδομένων
Είναι υπεύθυνο για την αποθήκευση και την διαχείριση των δεδομένων του συστήματος. Το συγκεκριμένο επίπεδο επικοινωνέι με το
επίπεδο της επιχειρηματικής λογικής ώστε να γίνει εισαγωγή, ανάγνωση, τροποίηση η διαγραφή κάποιων δεδομένων. Η αποθήκευση συνήθως γίνεται
σε μια βάση δεδομένων όπως η SQL. Στην περιπτωσή μας θα ήταν συνετό να χρησιμοποιήσουμε μια σχεσιακή βάση δεδομένων όπως η MariaDB για να μπορέσουμε
απεικονίσουμε τις συσχετίσεις που πιθανόν να υπάρχουν στο σύστημα μας. H Σύνδεση με την βάση θα μπορούσε να γίνει με κάποιον driver όπως είναι ο JDBC
που μας επιτρέπει να συνδεθούμε και να αλλιλεπιδράσουμε μετην βάση μέσω της γλώσσας προγραμματισμού java 

### Παράδειγμα ενός πίνακα SQL για τον λογαριασμό στο Τραπεζικό Σύστημα.
```SQL
CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `pin` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `balance` double NOT NULL
);
```

### Αναπαράσταση Πίνακα στην Επιχειρηματική Λογική με μια κλάση
```java
class Account {
    private int id;
    private int pin;
    private String name;
    private double balance;
}
```

## Τύποι Δεδομένων προτωκόλλου επικοινωνίας
* Τύπος αιτήματος RequestType (Αυθεντικοποίηση, κατάθεση, ανάληψη, υπόλοιπο, εγγραφή, αποσύνδεση)
```java
enum RequestType{
  auth,
  deposit,
  withdraw,
  checkBalance,
  register,
  logout
}
```
* Αντικείμενο αιτήματος
```java
class Request {
    private RequestType type;
    private int id;
    private int pin;
    private double ammount;
}
```

* Αντικείμενο απόκρισης
```java
class Response {
    private int id;
    private String name;
    private double balance;
    private String message;
    private boolean ok;
}
```

## Προτώκoλλο επικοινωνίας Πελάτη - Διακομιστή
Ο πελάτης μπορεί να εκτελέση της εξής ενέργειες
* Εγγραφή
* Σύνδεση/αυθεντικοποίηση
* κατάθεση
* ανάθεση
* Υπόλοιπο
* Αποσύνδεση

### Ψευδωκώδικας Αιτημάτων
```java
Request register (id, pin, name){
    this.type = Register;
    this.id = id;
    this.pin = pin;
    this.name = name;
}

Request Authentication (id, pin){
    this.type = Auth;
    this.id = id;
    this.pin = pin;
}

Request deposit (amount){
    this.type = Deposit;
    this.amount = amount;   
}

Request withdraw (amount){
    this.type = Withdraw;
    this.amount = amount
}

Request baalance (){
    this.type = balance;
}

Request logout (){
    this.type = Logout;
}
```
### Ψευδωκώδικας Αποκρίσεων
```java 
Response Success (){
    this.ok = true
}

Response Failed (){
   this.ok = false
   // Εξιδεικευμένα μηνύματα ανάλογα το λάθος
   this.message = "Error occurred"
}

Response Auth (){
    this.ok = true;
    // Επιστρέφουμε το όνομα του χρήστη ώστε να το δείξουμ στην οθόνη
    this.name = name
}

Reposnse Balance (){
    this.ok = true;
    this.balance = balance;
}

```

## Ψευδωκόδικας Υπηρεσιών της Βάσης Δεδομένων
```java 
class DB_Service {
    
    Account Create (id, pin, name){
        account = INSERT TO ACCOUNT VALUES (id, pin, name, 0)
        return account;
    }
    Account Read (id){
        account = SELECT * FROM ACCOUNT WHERE this.id = id;
        return account;
    }
    Account Update (id, balace) {
        account = UPDATE ACCOUNT SET this.balabce = balancd WHERE this.id = id;
        return account;
    }
    
    Account Delete (id){
        DELETE FROM ACCOUNT WHERE this.id = id; 
    }
    

}
```




