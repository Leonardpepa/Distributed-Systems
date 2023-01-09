# An implementation of the ATM Service using rest endpoints

## How to run
Just type
```terminal
  docker comppose up
```
And the app will be live at http://localhost:3000

## Technologies Used
* Nodejs
* Expressjs
* MongoDB
* Mongo-Store (to store the sessions)
* Passportjs
* ejs (templating language)

## Endpoints
```terminal 
// Router

// Page Controller responds with the html pages
app.get("/", pageController.home);
app.get("/logout", authController.logout);
app.get("/register", pageController.register);
app.get("/login", pageController.login);

// Auth Controller is responsible for the authentication system
app.post("/register", authController.register);
app.post("/login", authController.login);

// Service Controller is responsible for the atm services

// Authenticated routes
app.get("/user/balance", ensureAuth, serviceController.balance);
app.get("/user/info", ensureAuth, serviceController.info);
app.get("/user/statements", ensureAuth, serviceController.statementInfo);


// Authenticated routes
app.post("/user/deposit", ensureAuth, serviceController.deposit);
app.post("/user/withdraw", ensureAuth, serviceController.withdraw);
app.post("/user/transfer", ensureAuth, serviceController.transfer);
```
