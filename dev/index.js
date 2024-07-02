const express = require("express");

const router = express();

router.get("/", (req, res) => {
    res.send(JSON.stringify({
        url: "https://www.dropbox.com/scl/fi/5rltym7xax4jwheiuy5vs/Create-Custom.rar?rlkey=x45y4qww3583cmx993enn1zbj&st=4hlddsqs&dl=1",
        version: "00000"
    }))
});

router.listen(3000);