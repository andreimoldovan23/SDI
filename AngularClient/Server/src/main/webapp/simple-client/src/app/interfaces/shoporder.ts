import { Client } from './client';
import { Coffee } from './coffee';

export interface ShopOrder {
    id: number,
    client: Client,
    coffee: Coffee,
    status: string,
    time: string
}