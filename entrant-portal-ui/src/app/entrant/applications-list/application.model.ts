export interface Application {
  id: number,
  user: {
    id: number,
    login: string,
    role: string
  },
  course: {
      id: number,
      faculty: {
          id: number,
          name: string
      },
      name: string,
      seats: number,
      leftSeats: number;
  },
  priority: number,
  rating: number,
  position: number,
  status: string,
  examScore: number
}