package bg.softuni.cinevault.service;


import bg.softuni.cinevault.entities.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    void addReview(UUID movieId, UUID userId,Integer rating ,String comment);
    List<Review> getMovieReviews(UUID movieId);
    List<Review> getUserReviews(UUID id);
    void deleteReviewsByMovieId(UUID movieId);
}
