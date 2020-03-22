#!/bin/bash -e

./mongodb-cli.sh -i <<END
db.customerView.remove({});
db.customerView.count();
END


./mysql-cli.sh -i <<END
delete from customer;
END

# select count(*), max(creation_time) - min(creation_time), max(creation_time), min(creation_time) from customer where creation_time is not null;
# db.customerView.aggregate([ {     $group: {       _id: null,       max: {         $max: "$creationTime"       },       min: {         $min: "$creationTime"       }      }   } ]);
