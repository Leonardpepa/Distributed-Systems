const { getUserById, updateUser } = require("../db/user");
const {createStatement} = require("../db/statement.js");
const User = require("../models/user");
const UpdateLimitIfNeeded = require("../middleware/checkDailyLimit.js");
const serviceController = {
    info: async (req, res) => {
        
        UpdateLimitIfNeeded(req.user);

        const user = await getUserById(req.user._id);

        if (!user){
            return res.render("info", {user: null, error: "An error occurred while"});    
        }
        return res.render("info", {user: user, error: ""});
    },
    statementInfo: async (req, res) => {
        UpdateLimitIfNeeded(req.user);
        
        const user = await getUserById(req.user._id);

        if (!user){
            return res.render("statementinfo", {user: null, error: "An error occurred while"});    
        }
        return res.render("statementinfo", {user: user, error: ""});

    },
    balance: async (req, res) => {
        UpdateLimitIfNeeded(req.user);
        
        const user = await getUserById(req.user._id);
        
        if (!user) {
            return res.json({balance: -1, error: "An error occurred"});
        }
        return res.json({balance: user.balance, error: ""});
    },
    deposit: async (req, res) => {
        UpdateLimitIfNeeded(req.user);
        
        const amount = req.body.amount;

        if(amount <= 0){
            return res.json({balance: -1, error: "Amount cannot be negative"});
        }

        const user = await getUserById(req.user._id);

        const updated = await updateUser(req.user._id, {balance: user.balance + amount});

        if (!updated) {
            return res.json({balance: -1, error: "An error occurred"});
        }

        stmt = await createStatement("Deposit", `Successfully deposit ${amount}`);

        if (stmt) {
            await User.findOneAndUpdate(
                { _id: req.user._id },
                { $push: { statements: stmt._id } }
              );
        }
        return res.json({balance: updated.balance, error: ""});
    },

    withdraw: async (req, res) => {
        UpdateLimitIfNeeded(req.user);

        const amount = req.body.amount;

        if(amount <= 0){
            return res.json({balance: -1, error: "Amount cannot be negative or zero"});
        }

        const user = await getUserById(req.user._id);

        if (user && (user.limit - amount < 0)){
            return res.json({balance: -1, error: `Daily limit cannot be exceeded, you can only spend ${user.limit} more for today.`});
        }

        if (user && (user.balance - amount < 0)){
            return res.json({balance: -1, error: "Balance is not enough to complete the request"});
        }

        const updated = await updateUser(req.user._id, {balance: user.balance - amount, limit:user.limit - amount});
        
        if (!updated) {
            return res.json({balance: -1, error: "An error occurred"});
        }

        stmt = await createStatement("Withdraw", `Withdraw ${amount} successfully`);

        if (stmt) {
            await User.findOneAndUpdate(
                { _id: req.user._id },
                { $push: { statements: stmt._id } }
              );
        }

        return res.json({balance: updated.balance, error: ""});
    }


};
module.exports = serviceController;