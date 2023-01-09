const Mongoose = require("mongoose");
const passportLocalMongoose = require("passport-local-mongoose");
const findOrCreate = require("mongoose-findorcreate");
const userSchema = new Mongoose.Schema({
  //email
  username: {
    type: String,
    required: true,
    unique: true,
  },
  password: {
    type: String,
  },

  balance: {
    type: Number,
    default: 0,
  },
  limit: {
    type: Number,
    default: 1000,
  }, 
  dateLimit: {
    type: Date,
    default: Date.now,
  },
  statements: [
    {
      type: Mongoose.Schema.Types.ObjectId,
      ref: "Statement",
    },
  ],

});

userSchema.plugin(passportLocalMongoose);
userSchema.plugin(findOrCreate);
const User = new Mongoose.model("User", userSchema);

module.exports = User;