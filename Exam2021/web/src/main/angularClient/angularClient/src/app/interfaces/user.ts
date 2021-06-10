import { Follower } from "./follower";
import { Post } from "./post";

export interface User {
    name: string,
    posts: Post[],
    followers: Follower[]
}