-entities: AppUser, Followers, Posts, Comments, Address\
-persistence: Data Jpa, Hibernate, MySql\
-relationships: each user and follower have an address, \
a user has followers and posts and each post has comments\
-client: angular\
-endpoints: one for viewing user details, one for user details plus followers details, one for\
user details + follower details + posts/comments details, one for all the above, depends on the received data
