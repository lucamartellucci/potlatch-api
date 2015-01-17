INSERT INTO user(id,email,username,password,birthdate,gender,block_inappropriate,refresh_interval,roles) values (1,'luca.martellucci@gmail.com', 'luca', 'password01', '1978-09-25', 'M', 1, 60, 'ADMIN,USER' );
INSERT INTO user(id,email,username,password,birthdate,gender,block_inappropriate,refresh_interval,roles) values (2,'erica.cassina@gmail.com', 'erica', 'password01', '1982-03-02', 'F', null, 30, 'ADMIN,USER');

INSERT INTO gift(id,title,description,timestamp,parent_id,uuid,user_id,status,number_of_likes) values (1,'title_1','description_1','2014-12-01 12:33:01',NULL,'f6aa4067-5b21-4d98-b172-307b557187f0',1,'active',2);
INSERT INTO gift(id,title,description,timestamp,parent_id,uuid,user_id,status) values (2,'title_2','description_2','2014-12-01 12:31:01',NULL,'7cfa9ed8-5bd8-493f-9528-c45f8176103c',1,'active');
INSERT INTO gift(id,title,description,timestamp,parent_id,uuid,user_id,status) values (3,'title_3','description_3','2014-12-01 11:33:01',1,'a6c1e839-d390-4b37-9837-dee63b3cffd9',2,'active');
INSERT INTO gift(id,title,description,timestamp,parent_id,uuid,user_id,status) values (4,'title_4','description_4','2014-12-01 11:31:01',1,'051137b8-f1aa-444a-a1b4-07d284ccd647',2,'active');
INSERT INTO gift(id,title,description,timestamp,parent_id,uuid,user_id,status,number_of_likes) values (5,'title_5','description_5','2014-12-01 11:30:01',NULL,'5a4dcd02-1e6d-4c4d-a3a0-2e28cb631d21',1,'active',1);


INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (1, 1, 1, 0);
INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (1, 2, 0, 1);
INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (2, 1, 1, 0);
INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (2, 5, 1, 0);