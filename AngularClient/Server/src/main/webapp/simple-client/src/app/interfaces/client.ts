import { Address } from './address';

export interface Client {
    id: number,
    firstName: string,
    lastName: string,
    age: number,
    phoneNumber: string,
    address: Address
}