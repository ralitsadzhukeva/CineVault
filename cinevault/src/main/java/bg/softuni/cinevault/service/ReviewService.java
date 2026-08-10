package bg.softuni.cinevault.service;


import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.entities.User;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    void addReview(UUID movieId, UUID userId,Integer rating ,String comment);
    List<Review> getMovieReviews(UUID movieId);
    List<Review> getUserReviews(UUID id);
    void deleteReviewsByMovieId(UUID movieId);
    double getAverageRating(UUID movieId);
    void deleteReview(UUID id, User currentUser);

    void editReview(UUID id, User currentUser, Integer rating, String comment);

    Review findById(UUID id);
    Review findByIdForEdit(UUID id, User currentUser);
}
