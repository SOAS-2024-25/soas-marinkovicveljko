TRADE SERVICE

POST - BUY

http://localhost:8765/trade/buy

Body:

{
“email”: “user@uns.ac.rs”,
“cryptoSymbol”: “BTC”,
“amount”: 0.001,
“currency”: “EUR”,
“type”: “BUY”
}

POST – SELL

http://localhost:8765/trade/sell

Body:

{
“email”: “user@uns.ac.rs”,
“cryptoSymbol”: “BTC”,
“amount”: 0.0005,
“currency”: “EUR”,
“type”: “SELL”
}

Authorization:
username: user@uns.ac.rs
password: userPassword



CRYPTO CONVERSION

POST

http://localhost:8765/crypto-conversion/convert?email=user@uns.ac.rs&fromSymbol=BTC&toSymbol=ETH&amount=0.0002
http://localhost:8765/crypto-conversion/convert?email=user@uns.ac.rs&fromSymbol=ETH&toSymbol=BTC&amount=0.01

Authorization:
username: user@uns.ac.rs
password: userPassword



CRYPTO EXCHANGE

GET

http://localhost:8765/crypto-exchange/value?crypto=BTC&currency=EUR
http://localhost:8765/crypto-exchange/value?crypto=ETH&currency=USD

Authorization:
username: owner@uns.ac.rs || admin@uns.ac.rs || user@uns.ac.rs
password: ownerPassword || adminPassword || userPassword



CRYPTO WALLET

GET - all wallets

http://localhost:8765/crypto-wallets

Authorization:
username: admin@uns.ac.rs
password: adminPassword

GET - single user wallet

http://localhost:8765/crypto-wallets/email?email=user@uns.ac.rs

Authorization:
username: user@uns.ac.rs
password: userPassword

POST

http://localhost:8765/crypto-wallets

Body:

{
“email”: “testwallet@uns.ac.rs”,
“cryptoSymbol”: “XRP”,
“balance”: 10
}

Authorization:
username: admin@uns.ac.rs
password: adminPassword

PUT

http://localhost:8765/crypto-wallets/testwallet@uns.ac.rs/XRP

Body:

{
“email”: “testwallet@uns.ac.rs”,
“cryptoSymbol”: “XRP”,
“balance”: 25
}

Authorization:
username: admin@uns.ac.rs
password: adminPassword

DELETE

http://localhost:8765/crypto-wallets/email?email=testwallet@uns.ac.rs

Authorization:
username: admin@uns.ac.rs
password: adminPassword



BANK ACCOUNT

GET - all bank accounts

http://localhost:8765/bank-accounts

Authorization:
username: admin@uns.ac.rs
password: adminPassword

GET - single user bank account

http://localhost:8765/bank-accounts/email?email=user@uns.ac.rs

Authorization:
username: user@uns.ac.rs
password: userPassword

POST

http://localhost:8765/bank-accounts

Body:

{
“email”: “testbank@uns.ac.rs”,
“fiatBalances”: []
}

Authorization:
username: admin@uns.ac.rs
password: adminPassword

PUT

http://localhost:8765/bank-accounts

Body:

{
“email”: “testbank@uns.ac.rs”,
“fiatBalances”: [
{
“currency”: “EUR”,
“amount”: 210
},
{
“currency”: “USD”,
“amount”: 125
},
{
“currency”: “GBP”,
“amount”: 190
},
{
“currency”: “CHF”,
“amount”: 250
},
{
“currency”: “RSD”,
“amount”: 800
}
]
}

Authorization:
username: admin@uns.ac.rs
password: adminPassword

DELETE

http://localhost:8765/bank-accounts/email?email=testbank@uns.ac.rs

Authorization:
username: admin@uns.ac.rs
password: adminPassword


CURRENCY CONVERSION

GET

http://localhost:8765/currency-conversion?email=user@uns.ac.rs&from=CHF&to=RSD&quantity=100
http://localhost:8765/currency-conversion?email=user@uns.ac.rs&from=GBP&to=EUR&quantity=100

Authorization:
username: user@uns.ac.rs
password: userPassword



CURRENCY CONVERSION FEIGN

GET

http://localhost:8765/currency-conversion-feign?email=user@uns.ac.rs&from=EUR&to=RSD&quantity=10
http://localhost:8765/currency-conversion-feign?email=user@uns.ac.rs&from=USD&to=EUR&quantity=20

Authorization:
username: user@uns.ac.rs
password: userPassword


CURRENCY EXCHANGE

GET

http://localhost:8765/currency-exchange?from=CHF&to=RSD
http://localhost:8765/currency-exchange?from=USD&to=EUR

Authorization:
username: owner@uns.ac.rs || admin@uns.ac.rs || user@uns.ac.rs
password: ownerPassword || adminPassword || userPassword


USER SERVICE

GET - all users

http://localhost:8765/users

Authorization:
username: admin@uns.ac.rs || owner@uns.ac.rs
password: adminPassword || ownerPassword

GET - single user

http://localhost:8765/users/email?email=user@uns.ac.rs

Authorization:
username: admin@uns.ac.rs || owner@uns.ac.rs
password: adminPassword || ownerPassword

POST

http://localhost:8765/users/newUser

Body:

{
“email”: “testuser@uns.ac.rs”,
“password”: “test123”,
“role”: “USER”
}

Authorization:
username: admin@uns.ac.rs || owner@uns.ac.rs
password: adminPassword || ownerPassword

http://localhost:8765/users/newAdmin

Body:

{
“email”: “testadmin@uns.ac.rs”,
“password”: “novi123”,
“role”: “ADMIN”
}

Authorization:
username: owner@uns.ac.rs
password: ownerPassword

PUT

http://localhost:8765/users/email/testadmin@uns.ac.rs

Body:

{
“password”: “blabla”,
“role”: “ADMIN”
}

Authorization:
username: owner@uns.ac.rs
password: ownerPassword

DELETE

http://localhost:8765/users/email?email=testuser@uns.ac.rs
http://localhost:8765/users/email?email=testadmin@uns.ac.rs

Authorization:
username: owner@uns.ac.rs
password: ownerPassword