const { getUserById, createUser, getUserByUsername } = require("../db/user");
const passport = require("passport");
const User = require("../models/user");
const { passportAuthenticateLocal } = require("../middleware/ensureAuth");

const authController = {
  login: async (req, res, next) => {
    const { username, password } = req.body;
    try {
      const user = new User({
        username,
        password,
      });

      await req.login(user, (err) => {
        if (err) {
          res.redirect("/login?e=An unexpected error ocured please try again");
          return;
        }
        passportAuthenticateLocal(req, res);
      });
    } catch (error) {
      res.redirect("/login?e=An unexpected error ocured please try again");
    }
  },
  register: async (req, res, next) => {
    try {
      const { username, password } = req.body;
  
      if (username.length === 0 || password.length === 0){
        return res.redirect("/register?e=Emaill or Password cannot be empty")  
      }
      return await createUser(req, res, username, password);
    } catch (error) {
      return res.redirect("/register?e=Wrong format");  
    }
  },
  logout: async (req, res, next) => {
    req.logout((err) => {
      req.session.destroy(function (err) {
        return res.redirect('/'); 
      });
    });
  },
};

module.exports = authController;