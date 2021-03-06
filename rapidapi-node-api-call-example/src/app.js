// Imports needed modules
const path = require('path');
const express = require('express');
const axios = require("axios");
const hbs = require('hbs');
const YodaTranslator = require('./utils/yodaTranslator')
//const AstrologyHoroscope = require('./utils/astrologyHoroscope')
const getRandomInt = require('./utils/getRandomInt')

// Loads env variables
require('dotenv').config()

// Creates app
const app = express();

// Adds json parsing middleware
app.use(express.json());

// Initializes application port
const port = process.env.PORT || 3000;

// Define paths for Express config
const viewsPath = path.join(__dirname,'./templates/views');
const partialsPath = path.join(__dirname, './templates/partials')

// Setup handlebars engine and views location
app.set('view engine', 'hbs');
app.set('views', viewsPath);
hbs.registerPartials(partialsPath);

// Setup static directory to serve
app.use(express.static(path.join(__dirname, '../public')));

// Creates base URL route "/" and renders index view
app.get('', (req,res) => {
    res.render('index', {
        title: 'Yoda Horoscope',
    })
})

// Creates send-sms endpoint
app.post('/yodascope', async (req, res) => {
    const { 
        yourName, 
        yourBirthday,
        theirName,
        theirBirthday 
    } = req.body

    /*
    if (!yourName || !yourBirthday || !theirName || !theirBirthday) {
        return res.status(404).send({
            error: "Please provide all information"
        })
    }
    */

    try {
        
        axios.get('http://localhost:8080/zodiac_compatibility/result').then(resp => {
            console.log(resp.data);
            return res.json(resp.data)
        });

        //return res.json({ heading: 'geek', message: 'guy' })

    } catch(e) {
        console.log(e)

        return res.status(500).json({
            error: "Something went wrong"
        })
    }
})

// Catch all route, renders 404 page
app.get('*', (req, res) => {
    res.render('404',
        {
            search: 'page'
        }
    )
})

// Directs app to listen on port specified previously
app.listen(port, () => {
    console.log('Server is up on port ' + port)
})