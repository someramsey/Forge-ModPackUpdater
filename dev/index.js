const express = require("express");

const router = express();

router.get("/", (req, res) => {
    res.send(JSON.stringify({
        url: "https://www.google.com",
        version: "dw"
    }))
});

router.listen(3000);