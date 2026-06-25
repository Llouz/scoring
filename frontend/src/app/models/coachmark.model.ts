import { Guide } from './guide.model';
import { User } from './user.model';

export interface Coachmark {
  user: User;
  guide: Guide;
  score: number;
}
