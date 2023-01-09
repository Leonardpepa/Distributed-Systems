const express = require("express");
const app = express();

const ejs = require("ejs");
const session = require("express-session");
const MongoStore = require('connect-mongo');
const dotenv = require('dotenv').config()
const passport = require("./passportConfig.js")
// initialize database connection
require("./dbConnection.js")();

app.set("view engine", "ejs");
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(__dirname + "/public"));
app.use(
  session({
    secret: process.env.SECRET,
    resave: false,
    saveUninitialized: false,
    cookie: {
      httpOnly: true,
    },
    store: MongoStore.create({
      mongoUrl: process.env.MONGO_DB_URL,
      crypto: {
        secret: process.env.SECRET
      }
    })
  })
);

app.use(passport.initialize());
app.use(passport.session());


const pageController = require("./controllers/pageController.js");
const authController = require("./controllers/authController.js");
const serviceController = require("./controllers/serviceController.js");

const {ensureAuth} = require("./middleware/ensureAuth.js");


// Router
app.get("/", pageController.home);
app.get("/logout", authController.logout);
app.get("/login", pageController.login);
app.get("/register", pageController.register);

app.post("/register", authController.register);
app.post("/login", authController.login);

// Authenticated routes
app.get("/user/balance", ensureAuth, serviceController.balance);
app.get("/user/info", ensureAuth, serviceController.info);
app.get("/user/statements", ensureAuth, serviceController.statementInfo);


// Authenticated routes
app.post("/user/deposit", ensureAuth, serviceController.deposit);
app.post("/user/withdraw", ensureAuth, serviceController.withdraw);
app.post("/user/transfer", ensureAuth, serviceController.transfer);


const port = process.env.PORT || 3000;

app.listen(port, () => {
    console.log(`Live at http://localhost:${port}`);
});