const express = require("express");

const router = express();

router.get("/", (req, res) => {
    res.send(JSON.stringify({
        url: "https://www.dropbox.com/scl/fi/gf5fhdw2r8xaap48ouss4/dev.zip?rlkey=35wskehjm8uxn9fet53872hj9&st=d8hvilii&dl=1",
        version: "00000"
    }))
});

router.listen(3000);