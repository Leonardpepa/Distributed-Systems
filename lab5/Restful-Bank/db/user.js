const User = require("../models/user");
const { passportAuthenticateLocal } = require("../middleware/ensureAuth");
const { createStatement } = require("../db/statement.js");

const getUserById = async (id) => {
    try {
      const user = await User.findById(id, { hash: 0, salt: 0 }).populate("statements");
      return await user;
    } catch (error) {
      console.log(error);
    }
  };

  const createUser = async (
    req,
    res,
    username,
    password,
  ) => {
    try {
      await User.register(
        { username },
        password,
        (err, user) => {
          if (err) {
            res.redirect("/register?e=" + err.message);
            return;
          }
          passportAuthenticateLocal(req, res);
        }
      );
    } catch (error) {
      res.redirect("/register?e=An unexpected error ocured please try again");
    }
  };

  const getUserByUsername = async (username) => {
    try {
      const user = await User.findOne({ username });

      return await user;
    } catch (error) {
      console.log(error);
    }
  };
  
  const updateUser = async (id, fieldsToUpdate) => {
    try {
      const user = await User.findOneAndUpdate({ _id: id }, { ...fieldsToUpdate });

      return user;
    } catch (error) {
      console.log(error);
    }
  };



  module.exports = {getUserById, createUser, getUserByUsername, updateUser}