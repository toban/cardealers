db.createCollection("dealers", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: [ "id" ],
         properties: {
            id: {
               bsonType: "string",
               description: "must be a string (uuid) and is required"
            }
        }
      }
    }
})