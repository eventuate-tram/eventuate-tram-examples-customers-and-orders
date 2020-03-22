#!/bin/bash -e

echo 'db.customerView.aggregate([ {     $group: {       _id: null,       max: {         $max: "$creationTime"       },       min: {         $min: "$creationTime"       }      }   } ]);' | ./mongodb-cli.sh -i

./mysql-cli.sh -i <<END
select count(*), max(creation_time) - min(creation_time), max(creation_time), min(creation_time) from customer where creation_time is not null;
END
