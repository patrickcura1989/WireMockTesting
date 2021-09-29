const axios = require("axios");

const BASE_URL = `http://localhost:8080`

module.exports = {
    getCompatibility: (yourName, yourBirthday, theirName, theirBirthday) => axios({
        method:"GET",
        url : BASE_URL + `/zodiac_compatibility/result`
    })
}
