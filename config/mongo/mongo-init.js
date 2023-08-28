use spring_base;

db.createUser({
    user: 'normal_user',
    pwd: 'normal_password',
    roles: [{ role: 'readWrite', db: 'spring_base'}]
});

db.createCollection('discount');
