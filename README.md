PO CRM stub API
===============

### 1. POST /api/fetchInfo

example request:
```
{
	"addresses": [
		"custA@gmail.com",
		"custB@gmail.com",
		"custC@gmail.com"
	],
	"username": "djordjec",
	"signature": "1bc29b36f623ba82aaf6724fd3b16718"
}
```

example response:
```
{
	"customerId": "123",
	"customerName": "Customer A",
	"opportunities": [
		{
			"amount": "10000",
			"date": "2018-03-14 00:00:00",
			"status": "PENDING"
		}
	]
}
```


-----------------

### 2. POST /api/sync

example request:
```
{
	"messages": [
		{
			"sender": "djordjec@gmail.com",
			"recipient": [
				"dulbec@gmail.com",
				 "custB@gmail.com"
			],
			"subject": "A business offer...",
			"body": "something something dark side",
			"date": "2018-02-07 00:00:00"
		},
		{
			"sender": "custB@gmail.com",
			"recipient": "djordjec@gmail.com",
			"subject": "RE: A business offer...",
			"body": "something something cookies",
			"date": "2018-02-08 12:00:00"
		}
	],
	"username": "djordjec",
	"signature": "028ba9af611925a0064012a6e6fd765b"
}
```

example response:
```
{
      "requested": "2",
      "inserted": "2",
      "threadId": "1",
      "emailId": [
          "1",
          "2"
      ]
}
```
