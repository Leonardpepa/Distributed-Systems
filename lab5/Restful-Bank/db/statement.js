const Statement = require("../models/statement");

const createStatement = async (statement_type, message) => {

    stmt = await new Statement({statement_type: statement_type, message: message}).save();

    return  stmt;

}



module.exports = {createStatement};