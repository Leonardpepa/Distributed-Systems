const pageController = {
    login: (req, res) => {
      return res.render("login",  { error: { generic: req.query.e } });
    },
    register: (req, res) => {
      return res.render("register",  { error: { generic: req.query.e } });
    },
    home: (req, res) => {
      if (req.isAuthenticated()) {
        return res.render("home",  { error: { generic: req.query.e }, user: req.user});
      }else{
        res.redirect("/login");
      }
    }
};
module.exports = pageController;