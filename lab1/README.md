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

## Συσχέτιση με το μοντέλο MVC (MODEL, VIEW, CONTROLLER)
Για την σχεδίαση χρησιμοποιήθηκε το μοντέλο MVC που συνάδει και με την αρχιτεκτονική των τριών επιπέδων. <br> 
Οι περισσότερες οντότητες αναλύονται παρακάτω

* VIEW (Επίπεδο Παρουσίασης)
  - loginPage
  - registerPage
  - mainPage
* CONTROLLER (Επιχειρηματική Λογική)
    - Server
    - ControllerThread
    - ServerProtocol
    - RequestType
    - Request
    - response
* MODEL (Επίπεδο Δεδομένων)
    - Account
    - DB_Service
    - DB_Connector

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

## Πρωτόκoλλο επικοινωνίας Πελάτη - Διακομιστή
Ο πελάτης μπορεί να εκτελέση τις εξής ενέργειες
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
Άπλες λειτουργίες CRUD (CREATE, READ, UPDATE, DELETE) <br>
Αν προκύψει το οποιοδήποτε λάθος τότε οι μέθοδοι επιστρέφουν null (τίποτα)
```java 
class DB_Service {

    connection = DB_Connector.getConnevntion();

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

## Πρωτόκολλο Διαδομιστή
Θέλουμε ο διακομιστής να μπορεί να εξυπηρετήσει πολλαπλούς πελάτες, για αυτόν τον λόγω 
θα πρέπει να σχεδιάσουμε εναν πολυνηματικό διακομιστή ο οποίος θα δέχεται συνδέσεις πελάτων 
θα δημιουργεί κατάλληλο νήμα για να τις διαχειριστεί. Το νήμα θα είναι υπεύθυνο για την σύνδεση με την βάση,
την ανταλλαγή μηνυμάτων αλλα και την επιχειρηματική λογική του συστήματος

### Ψευδωκώδικας Πολυνηματικού Διακομιστή
```java
class Server{
    
    create ServerSocket(PORT);
    
    while(true){
        clientSocket = ServerSocket.listen();
        create ControllerThread(clientSocket);
        controllerThread.start();
    }
}
```

### Ψευδωκώδικας για ControllerThread
```java
class ControllerThread extends Thread{
    ControllerThread(clientSocket){
        this.clientSocket = clientSocket;
        this.in = clientSocket.getInputStream();
        this.out = clientSocket.getOutputStream();
        // connection to database for each thread
        service = new DBService();
    }
    
    void run(){
        while (true){
            // get request
            Request request = in.getRequest();
            // create server protocol object
            ServerProtocol serverProtocol = new ServerProtol(request, service);
            // wait for response from server protocol
            Response response = serverProtocol.processRequest();
            // write response to clinet
            out.writeResponse(response);            
        }
    }
}
```

### Ψευδωκώδικας για ServerProtocol (Επιχειρηματική λογική και εντοπισμός σφαλμάτων)
```java
class ServerProtocol{
    ServerProtocol(request, service){
        processRequest(request);
    }
    
     public Response processRequest(request) {
        switch (request.getType()) {
            case auth:
                return processAuth();
            case register:
                return createAcc();
            case deposit:
                return processDeposit();
            case withdraw:
                return processWithdraw();
            case checkBalance:
                return processCheckBalance();
            case logout:
                return logout();
        }
    }
    
    private Response logout() {
        Response response = Response.createGeneralSuccessResponse();
        return response;
    }

    private Response processAuth() {
    
        Account acc = service.auth(id, pin);
    
        if (acc == null) {
            return Response Error("Authentication failed");
        }

        return Response AuthSuccess(acc.name)
    }

    private Response createAcc() {
        Account foundAccount = service.read(id);
    
        if (foundAccount != null) {
            return  Response Error("Account already exists");
        }
        
        lock(id);

        Account account = new Account(id, pin, name, 0);

        if (service.create(account) == null) {
            return  Response Error("Account creation failed please try again");
        }
        
        unlock(id);
        return Response Success();

    }

    private Response processDeposit() {
        
        lock(id);
        
        double amountToDeposit = request.getAmount();
        
        if (amountToDeposit == 0) {
            return Reposponse Error("The amount cannot be 0");
        }
        Account accountFound = service.read(id);

        if (accountFound == null) {
            return Response Error("There was an error with the request");
        }

        accountFound.deposit(amountToDeposit);

        if (service.update(accountFound) == null) {
            return Response Error("Deposit failed");
        }
        
        unclock(id);
        return Response Success();
    }

    private Response processWithdraw() {    
        lock(id);    
        double amountToWithdraw = request.getAmount();

        if (amountToWithdraw == 0) {
            return Response Error("The amount cannot be 0");
        }
        
        Account accountFound = service.read(id);
        
        if (accountFound == null) {
            return Response Error("There was an error with the request");
        }

        if (accountFound.getBalance() - amountToWithdraw < 0) {
            return Response Error("Balance is not enough Withdrawal failed");
        }
        
        accountFound.withdraw(amountToWithdraw);
        
        if (service.update(accountFound) == null) {
            return Response Error("Withdrawal failed");
        }
        unclock(id);
        return Response Success();
    }

    private Response processCheckBalance() {
        lock(id);
        Account account = service.read(id);
        
        if (account == null) {
            return Response Error("Server error");
        }
        unclock(id);
        return Response Balance(balance);
    
    }    
}
```
# Ενοπισμός Σφαλμάτων
Ο εντοπισμός σφαλμάτων αφόρα κυρίως τα δεδομένα που στέλνει ο πελάτης σχετικά με την ενέργεια
που θέλει να ολοκληρώσει. Η σωστή δομή των μηνυμάτων επιτυχγάνεται καθώς στέλνουμε αντικείμενα 
request και response μεταξύ του πελάτη και του διακομιστή, που έχουν μια γνωστή δομή. Αν προκύψει λάθος
στην δομή του μηνύματος θα πιαστεί απο κάποιο exception της γλώσσας προγραμματισμού.


## Ενοπισμος Σφαλμάτων Στον διακομιστή
Όπως φαίνεται παραπάνω στον ψευδωκώδικα γίνεται έλεγχος στον διακομιστή 
σέ κάθε ενέργεια που ζητάει να εκτελέση ο χρήστης. Πιο συγκεκριμένα γινόνται έλεγχει που αφορούν:

* Την αυθεντικοποίηση του χρήστη (ελέγχεται αν το id και το pin είναι έγκυρα και υπάρχουν στην βάση)
* Την Ανάληψη ενός ποσού (ελέχγεται αν επαρκεί το διαθέσιμο υπόλοιπο για να ολοκληρωθεί η διαδικασία)
* Την δημιουργία ενός χρήστη (ελέχγεται αν υπάρχει ηδη χρήστης με το ιδιο id)
* Γίνεται έλεγχος για κάθε απάντηση που παίρνουμε απο την βάση (Αν η απάντηση είναι null σημαίνει πως προέκυψε κάποιο σφάλμα)

## Ενοπισμος Σφαλμάτων Στον Πελάτη
Μπόρουμε να εέγχουμε για κάποια προφανή λάθη και στο επίπεδο της παρουσίασης ώστε να κάνουμε
το σύστημα ποιο εύχρηστο και να μην χρειαστεί να στέλνουμε λάθος δεδομένα στον σέρβερ να ελέγξει.
Τέτοιο έλεγχοι είναι οι εξής:
* Έλεγχος για το αν υπάρχουν κενές μεταβλήτες πχ κενό όνομα
* Έλεγχος για το αν το id και το pin είναι ακέραιοι αριθμοί

##  Πιθανές καταστάσεις (states) του πρωτοκόλλου
Ένα πρωτόκολλο θεωρείτε οτι έχει μια κατάσταση ειναι δηλαδή statefull, όταν το κάθε αίτημα
εξαρτάται απο την προηγούμενο απόκριση. Ενώ ένα πρωτόκολλο είναι stateless όταν κάθε αίτημα έίναι ανεξάρτητο 
απο κάθε προηγούμενη απόκριση. Καλό είναι να αποφεύγουμε τις εξαρτήσεις και το statefull πρωτόκολλο
καθώς είναι πιο δύσκολο στην ανάπτυξη και πιό επιρρεπή σε λάθη. Στην περίπτωση μας η μοναδική κατάσταση που θα μπορούσαμε
να έχουμε είναι η αυθεντικοποίηση του χρήστη, καθώς για να κάνει τις περισσότερες ενέργειες θα πρέπει να είναι συνδεδεμένος


# Προβλήματα Ταυτοχρονισμού
Προβλήματα ταυτοχρονισμού μπορούν να προκύψουν άν επιτρέπουμε παραπάνω απο μία σύνδεση
σε κοινό λογαριασμό. Συγκεκριμένα όταν 2 χρήστες να προσπαθούν
να πραγματοποιήσουν ανάληψη στον ίδιο λογαριασμό και δεν μήν επαρκεί το ποσό. Στην σχεδίαση μας 
διαχειριζόμαστε τέτοιες καταστάσεις στο επίπεδο της επιχειρηματικής λογικής με το να κλειδώνουμε το id του λογαριασμού που ξεκινά μια 
συναλλαγή και το απευλεθεώνουμε μόλις ολοκληρωθεί. Επίσης περαιτέρω έλεγχοι περνάνε στο επίπεδο των δεδομένων καθώς σχεδόν κάθε υλοποίηση
μιας βάσης δεδομένων έχει λειτουργίες ταυτοχρονισμού. Παραδείγαματα κλείδώματος υπάρχουν στον ψευδωκώδικα της επιχειρηματικής λογικής παραπάνω (ServeProtocol)




