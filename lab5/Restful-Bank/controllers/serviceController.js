const { getUserById, updateUser, getUserByUsername } = require("../db/user");
const {createStatement} = require("../db/statement.js");
const User = require("../models/user");
const UpdateLimitIfNeeded = require("../middleware/checkDailyLimit.js");
const serviceController = {
    info: async (req, res) => { 

        try{
            UpdateLimitIfNeeded(req.user);
    
            const user = await getUserById(req.user._id);
    
            if (!user){
                return res.render("info", {user: null, error: "An error occurred while"});    
            }
            return res.render("info", {user: user, error: ""});
        }catch(error){
            console.log(error);
        }
        
    },
    statementInfo: async (req, res) => {
        try {
            UpdateLimitIfNeeded(req.user);
            
            const user = await getUserById(req.user._id);
    
            if (!user){
                return res.render("statementinfo", {user: null, error: "An error occurred while"});    
            }
            return res.render("statementinfo", {user: user, error: ""});
        } catch (error) {
            console.log(error);
        }

    },
    balance: async (req, res) => {

        try {
            UpdateLimitIfNeeded(req.user);
            
            const user = await getUserById(req.user._id);
            
            if (!user) {
                return res.json({balance: -1, error: "An error occurred"});
            }
            return res.json({balance: user.balance, error: ""});
        } catch (error) {
            console.log(error);
        }
    },
    deposit: async (req, res) => {
        try {
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
        } catch (error) {
            console.log(error);
        }
    },

    withdraw: async (req, res) => {
        try {
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
        } catch (error) {
            console.log(error);
        }
    },

    transfer: async (req, res) => {

        try {
            const {username, amount} = req.body;
    
            if (username === req.user.username) {
                return res.json({balance: -1, error: "Cannot tranfer money to your account!"});
            }
    
    
            if (username.length === 0){
                return res.json({balance: -1, error: "Username is required"});
            }
    
            if (amount <= 0){
                return res.json({balance: -1, error: "Amount Cannot be negative"});
            }
    
            const userToTransfer = await getUserByUsername(username);
    
            if (!userToTransfer){
                return res.json({balance: -1, error: "Account with that username doesn't exist"});
            }
    
            const updatedUser = await serviceController.withdrawHelper(req.user._id, amount);
            
            if (!updatedUser){
                return res.json({balance: -1, error: "An Error occurred"}); 
            }
    
            const updatedTransfer = await updateUser(userToTransfer._id, {balance: userToTransfer.balance + amount});   
    
            if (!updatedTransfer){
                return res.json({balance: -1, error: "An Error occurred"}); 
            }
            
            stmt = await createStatement("Transfer", `Transfered ${amount} successfully to ${updatedTransfer.username}`);
    
            if (stmt) {
                await User.findOneAndUpdate(
                    { _id: req.user._id },
                    { $push: { statements: stmt._id } }
                  );
            }
    
            return res.json({balance: updatedUser.balance, error: ""}); 
        } catch (error) {
            console.log(error);
        }

    },
    withdrawHelper: async (id, amount) => {
        try {
            if(amount <= 0){
                return null;
            }
    
            const user = await getUserById(id);
    
            if (user && (user.limit - amount < 0)){
                return null;
            }
    
            if (user && (user.balance - amount < 0)){
                return null;
            }
    
            const updated = await updateUser(id, {balance: user.balance - amount, limit:user.limit - amount});
            
            if (!updated) {
                return null;
            }
    
            return updated
        } catch (error) {
            console.log(error);
        }
    }

};
module.exports = serviceController;