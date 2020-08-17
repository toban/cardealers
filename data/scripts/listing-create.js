db.createCollection("listing", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: [ "id", "dealer", "code", "make", "model", "kW", "year", "color", "price" ],
         properties: {
            id: {
               bsonType: "string",
               description: "must be a string (uuid) and is required"
            },
            dealer: {
               bsonType: "string",
               description: "must be a string (uuid) and is required"
            },
            code: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            make: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            model: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            kW: {
               bsonType: "int",
               description: "must be an int and is required"
            },
            year: {
               bsonType: "int",
               description: "must be an int and is required"
            },
            color: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            price: {
               bsonType: "decimal",
               description: "must be a decimal and is required"
            },
         }
      }
   }
})

