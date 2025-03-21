export interface Order {
    orderId: number;
    user: {
        email: string;
    };
    books: {
        title: string;
        quantity: number;
    }[];
    totalPrice: number;
    status: string;
}
