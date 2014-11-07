INSERT INTO user(id,email,name,password,birthdate,gender) values (1,'luca.martellucci@gmail.com', 'luca', 'password01', '1978-09-25', 'M');
INSERT INTO user(id,email,name,password,birthdate,gender) values (2,'erica.cassina@gmail.com', 'erica', 'password01', '1982-03-02', 'F');

INSERT INTO gift(id,uuid,title,description,timestamp,parent_id,uri,user_id,status,number_of_likes) values (1,'f6aa4067-5b21-4d98-b172-307b557187f0','title_1','description_1',CURRENT_TIMESTAMP,NULL,'http://www.url1.it',1,'active',2);
INSERT INTO gift(id,uuid,title,description,timestamp,parent_id,uri,user_id,status) values (2,'7cfa9ed8-5bd8-493f-9528-c45f8176103c','title_2','description_2',CURRENT_TIMESTAMP,NULL,'http://www.url2.it',1,'active');
INSERT INTO gift(id,uuid,title,description,timestamp,parent_id,uri,user_id,status) values (3,'a6c1e839-d390-4b37-9837-dee63b3cffd9','title_3','description_3',CURRENT_TIMESTAMP,1,'http://www.url3.it',2,'active');
INSERT INTO gift(id,uuid,title,description,timestamp,parent_id,uri,user_id,status) values (4,'051137b8-f1aa-444a-a1b4-07d284ccd647','title_4','description_4',CURRENT_TIMESTAMP,1,'http://www.url4.it',2,'active');
INSERT INTO gift(id,uuid,title,description,timestamp,parent_id,uri,user_id,status,number_of_likes) values (5,'5a4dcd02-1e6d-4c4d-a3a0-2e28cb631d21','title_5','description_5',CURRENT_TIMESTAMP,NULL,'http://www.url5.it',1,'active',1);


INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (1, 1, 1, 0);
INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (1, 2, 0, 1);
INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (2, 1, 1, 0);
INSERT INTO user_action (user_id, gift_id, i_like_it, inappropriate) VALUES (2, 5, 1, 0);