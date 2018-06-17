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

  var query = request.query;

  //var ethAddress = query.eth;
  var contractAddress = query.contract;

  if(!contractAddress){
      contractAddress = request.body.contract;
  }

  if(!contractAddress){
    response.status(400).send({
      error: "missing contract address"
    });
    return;
  }

  var ref = admin.database().ref();
  var path = "/token/" + contractAddress;

/* this is solved in app
  ref.child(path).on("symbol", function (snapshot){

    response.status(200).send({
      symbol: snapshot.val(),
      contract: contractAddress
    });

  }, function (error){
      requestTokenSymbol(contractAddress, response);
  });
*/

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

                  ref.child(path).child("symbol").set(result);
                  ref.child(path).child("address").set(contractAddress);

                  response.status(200).send({
                    symbol: result,
                    contract: contractAddress
                  });
                } else if(err){
                  console.log("symbol : " + err);
                  response.status(500).send({
                    error: err,
                    contract: contractAddress
                  });
                } else {
                  console.log("symbol error! " + contractABI);
                  response.status(500).send({
                    error: "symbol not found",
                    contract: contractAddress
                  });
                }
              });
          } else {
            console.log("symbol error! " + contractABI);
            response.status(500).send({
              error: "symbol not found",
              contract: contractAddress
            });
          }
      } else {
          console.log("symbol error! " + contractABI);
          response.status(500).send({
            error: "symbol not found",
            contract: contractAddress
          });
      }
  });
});
