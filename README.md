# personal-finances-dashboard

#### Runs on localhost: 9005

### Fake login service

- In order to use the fakeLogin Service in developer mode run with

```shell
sbt run -Dconfig.resource=application.dev.conf
```



### To use g8Scaffold

Open sbt shell and choose one of the available Pages, for example: 

```shell
  g8Scaffold contentPage
```

in bash run migrate.sh
```shell
  ./migrate.sh

```

you may need to give it permissions:
```shell
  chmod +x migrate.sh
```

give the Page a Classname

```json
{
  "_id": "lananh.nguyen",
  "data": {
    "isUserClaimingChb": true,
    "children": [
      {
        "qualifiesForDla": true,
        "childsName": "harry potter",
        "childsBirthDate": "2023-02-02",
        "dlaRate": "lower"
      },
      {
        "childsName": "suzy q",
        "childsBirthDate": "2012-12-12",
        "qualifiesForDla": true,
        "dlaRate": "higher"
      }
    ]
  },
  "lastUpdated": {
    "$date": "2025-10-02T14:08:24.429Z"
  }
}
```