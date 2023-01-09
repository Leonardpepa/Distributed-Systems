const passport = require("passport");
const User = require("../models/user");

const ensureAuth = (req, res, next) => {
  if (req.isAuthenticated()) {
    next();
  } else {
    res.redirect("/");
  }
};

const passportAuthenticateLocal = (req, res) => {
  passport.authenticate("local", {
    failureRedirect: "/login?e=Email or Password are incorrect",
  })(req, res, async () => {
    res.redirect("/");
  });
};

module.exports = { ensureAuth, passportAuthenticateLocal };