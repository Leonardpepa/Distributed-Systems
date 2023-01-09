const Mongoose = require("mongoose");

const userSchema = new Mongoose.Schema({
    statement_type: {
        type: String,
    },
    message: {
        type: String,
    },
    timestamp: {
        type: Date,
        default: Date.now,
    }
});
  
const Statement = new Mongoose.model("Statement", userSchema);
  
module.exports = Statement;