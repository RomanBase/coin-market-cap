const firebase = require('firebase');
const functions = require('firebase-functions');
const url = require('url');

var admin = require("firebase-admin");
var serviceAccount = require("./google-service.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://coin-market-portfolio.firebaseio.com"
});

const Web3 = require('web3');
const web3 = new Web3(new Web3.providers.HttpProvider("https://mainnet.infura.io/Ni3MhgyqRjxkh6D0l596"));
const version = web3.version.api;

var jsdom = require('jsdom');
const { JSDOM } = jsdom;
const { window } = new JSDOM();
const { document } = (new JSDOM('')).window;
global.document = document;
var $ = require('jquery')(window);

exports.token = functions.https.onRequest((request, response) => {

  var url_parts = url.parse(request.url, true);
  var query = url_parts.query;

  var ethAddress = query.eth;
  var contractAddress = query.contract;

  var ether = 'https://api.etherscan.io/api?module=contract&action=getabi&address=' + contractAddress;

  console.log(ether);

  $.getJSON(ether, function (data) {
      var contractABI = "";
      contractABI = JSON.parse(data.result);

      if (contractABI != ''){
          var contract = web3.eth.contract(contractABI);
          var token = contract.at(contractAddress);

          if(token.address){
              console.log("address : " + token.address);
          }

          if(token.symbol){
              token.symbol((err, result) => {
                if(result){
                  console.log("symbol : " + result);

                  var ref = admin.database().ref();
                  var path = "/token/" + contractAddress;

                  console.log(path);

                  ref.child(path).child("symbol").set(result);
                  ref.child(path).child("address").set(contractAddress);

                  response.send(result);
                } else if(err){
                  console.log("symbol : " + err);
                  response.send(err);
                } else {
                  console.log("symbol error!");
                  response.send(contractABI);
                }
              });
          }

      } else {
          console.log("Error" );
          response.send(contractABI);
      }
  });
});
