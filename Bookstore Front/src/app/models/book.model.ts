export interface Review {
    id: number;
    userId: number;
    userName: string;
    rating: number;
    comment: string;
    createdAt: string;
}

export interface Book {
    id: number | null;
    title: string;
    author: string;
    description: string;
    category: string;
    quantity: number;
    price: number;
    imageUrl: string;
    reviews: Review[]; // Add this line
    averageRating: number; // Add this line
}
