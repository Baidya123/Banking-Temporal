
# Banking with Temporal

The project demonstrate the use of workflow using Temporal




## End Points
1. This api can transfer funds between accounts and demonstrates the use of temporal workflow integration 
http://localhost:6000/api/v1/account/fund-transfer




## Request Payload
For End point 1:
```javascript
{
  "debitAccount": {
    "accountNumber": 4
  },
  "creditAccount": {
    "accountNumber": 6
  },
  "payment": {
    "amount": 150
  }
}
```
Demo Response
```
true
```

## Deployment

Before deployment make sure to update the **application.properties** file with the host name and its port


```bash
truist.service-url.debit = http://localhost:7001/api/accounts/withdrawal
truist.service-url.credit = http://localhost:7002/api/accounts/deposit
truist.service-url.notify = http://localhost:7003/api/v1/checkBalance
truist.service-url.balance = http://localhost:7003/api/v1/checkBalance
truist.service-url.debit-rollback = http://localhost:7001/api/accounts/withdrawrollback
truist.service-url.credit-rollback = http://localhost:7002/api/accounts/depositrollback
```

