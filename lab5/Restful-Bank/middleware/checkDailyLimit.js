const { updateUser } = require("../db/user.js");

const UpdateLimitIfNeeded = async (user) => {
    try {
        const date1 = user.dateLimit;
        const date2 = Date.now();
        const difference_In_Time = date2 - date1.getTime();
        const difference_In_Days = Math.round(difference_In_Time / (1000 * 3600 * 24));
        if (difference_In_Days >= 1){
            const updated = await updateUser(user._id, {limit: 1000});
        }
    } catch (error) {
        console.log(error);
    }
}



module.exports = UpdateLimitIfNeeded;