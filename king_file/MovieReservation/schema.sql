CREATE TABLE `age_rating` (
  `age_rating_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `label` varchar(255) NOT NULL
);

CREATE TABLE `audit_log` (
  `log_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `actor_type` varchar(255) NOT NULL,
  `actor_id` bigint,
  `action` varchar(255) NOT NULL,
  `target_table` varchar(255) NOT NULL,
  `target_id` bigint,
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP),
  `detail` json
);

CREATE TABLE `city_region` (
  `city_region_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `city` varchar(255) NOT NULL,
  `district` varchar(255) NOT NULL
);

CREATE TABLE `distributor` (
  `distributor_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL
);

CREATE TABLE `language` (
  `language_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `label` varchar(255) NOT NULL
);

CREATE TABLE `member_tier` (
  `tier_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `min_spend` decimal(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE `payment_method` (
  `method_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `label` varchar(255) NOT NULL
);

CREATE TABLE `price_rule` (
  `rule_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `rule_type` varchar(255) NOT NULL,
  `priority` int NOT NULL DEFAULT 100,
  `amount_type` varchar(10) NOT NULL,
  `amount_value` decimal(10,2) NOT NULL,
  `active_from` datetime,
  `active_to` datetime
);

CREATE TABLE `promotion` (
  `promotion_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `active_from` datetime,
  `active_to` datetime
);

CREATE TABLE `subtitle` (
  `subtitle_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `label` varchar(255) NOT NULL
);

CREATE TABLE `tax` (
  `tax_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `rate_pct` decimal(10,2) NOT NULL,
  `effective_from` datetime,
  `effective_to` datetime
);

CREATE TABLE `theater` (
  `theater_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `city_region_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255)
);

CREATE TABLE `screen` (
  `screen_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `theater_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `layout_version` int NOT NULL DEFAULT 1
);

CREATE TABLE `screen_layout` (
  `layout_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `screen_id` bigint NOT NULL,
  `version` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `seat_type` (
  `seat_type_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `label` varchar(255) NOT NULL,
  `price_delta` decimal(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE `seat` (
  `seat_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `layout_id` bigint NOT NULL,
  `row_label` varchar(255) NOT NULL,
  `col_number` int NOT NULL,
  `seat_type_id` bigint NOT NULL
);

CREATE TABLE `show_status` (
  `show_status_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `label` varchar(255) NOT NULL
);

CREATE TABLE `movie` (
  `movie_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `duration_min` int NOT NULL,
  `released_on` date,
  `age_rating_id` bigint,
  `distributor_id` bigint
);

CREATE TABLE `movie_version` (
  `version_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `movie_id` bigint NOT NULL,
  `format` varchar(255) NOT NULL,
  `audio_lang_id` bigint NOT NULL,
  `subtitle_id` bigint NOT NULL
);

CREATE TABLE `showtime` (
  `show_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `screen_id` bigint NOT NULL,
  `version_id` bigint NOT NULL,
  `starts_at` datetime NOT NULL,
  `ends_at` datetime NOT NULL,
  `show_status_id` bigint NOT NULL
);

CREATE TABLE `price_rule_applies` (
  `rule_apply_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `rule_id` bigint NOT NULL,
  `theater_id` bigint,
  `screen_id` bigint,
  `seat_type_id` bigint,
  `day_of_week` int,
  `time_from` time,
  `time_to` time
);

CREATE TABLE `price` (
  `price_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `show_id` bigint NOT NULL,
  `seat_type_id` bigint NOT NULL,
  `default_price` decimal(10,2) NOT NULL,
  `changed_price` decimal(10,2),
  `rule_apply_id` bigint
);

CREATE TABLE `maintenance_ticket` (
  `ticket_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `seat_id` bigint NOT NULL,
  `opened_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP),
  `closed_at` datetime,
  `status` varchar(255) NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE `member` (
  `member_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255) UNIQUE NOT NULL,
  `name` varchar(255) NOT NULL,
  `tier_id` bigint,
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `hold_seat` (
  `hold_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `show_id` bigint NOT NULL,
  `seat_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `expires_at` datetime NOT NULL
);

CREATE TABLE `booking` (
  `booking_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `show_id` bigint NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'PENDING',
  `total_amount` decimal(10,2) NOT NULL DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `booking_seat` (
  `booking_id` bigint NOT NULL,
  `seat_id` bigint NOT NULL,
  `show_id` bigint NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`booking_id`, `seat_id`)
);

CREATE TABLE `payment` (
  `payment_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `method_id` bigint NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `status` varchar(255) NOT NULL,
  `approved_at` datetime
);

CREATE TABLE `coupon` (
  `coupon_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `promotion_id` bigint,
  `code` varchar(255) UNIQUE NOT NULL,
  `max_uses` int NOT NULL DEFAULT 1,
  `remaining` int NOT NULL DEFAULT 1,
  `discount_type` varchar(10) NOT NULL,
  `discount_value` decimal(10,2) NOT NULL,
  `valid_from` datetime,
  `valid_to` datetime
);

CREATE TABLE `coupon_redeem` (
  `redeem_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `coupon_id` bigint NOT NULL,
  `booking_id` bigint NOT NULL,
  `redeemed_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `refund` (
  `refund_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `payment_id` bigint,
  `amount` decimal(10,2) NOT NULL,
  `reason` varchar(255),
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `ticket` (
  `ticket_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `issued_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP),
  `qr_code` varchar(255)
);

CREATE TABLE `notification` (
  `notification_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `title` varchar(255) NOT NULL,
  `body` text,
  `sent_at` datetime,
  `channel` varchar(255)
);

CREATE TABLE `review` (
  `review_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `movie_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `content` text,
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `review_like` (
  `review_like_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `review_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `member_movie_preference` (
  `preference_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `genre_id` bigint,
  `preference_score` decimal(10,2) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `movie_similarity` (
  `movie_id_1` bigint NOT NULL,
  `movie_id_2` bigint NOT NULL,
  `similarity_score` decimal(10,2) NOT NULL,
  PRIMARY KEY (`movie_id_1`, `movie_id_2`)
);

CREATE TABLE `recommendation` (
  `recommendation_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `movie_id` bigint NOT NULL,
  `rank_score` decimal(10,2) NOT NULL,
  `generated_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `member_role` (
  `member_id` bigint NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`member_id`, `role`)
);

CREATE TABLE `member_login_history` (
  `login_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `login_time` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP),
  `ip_addr` varchar(255),
  `device_info` varchar(255)
);

CREATE TABLE `member_block_list` (
  `block_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `reason` varchar(255),
  `blocked_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `non_member` (
  `non_member_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `email` varchar(255),
  `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `booking_non_member` (
  `booking_id` bigint NOT NULL,
  `non_member_id` bigint NOT NULL,
  PRIMARY KEY (`booking_id`)
);

CREATE TABLE `snack_store` (
  `store_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `theater_id` bigint,
  `store_name` varchar(200) NOT NULL,
  `open_time` time,
  `close_time` time
);

CREATE TABLE `menu` (
  `snack_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `snack_name` varchar(200) NOT NULL,
  `snack_price` int NOT NULL,
  `snack_type` varchar(100)
);

CREATE TABLE `snack_order` (
  `order_id` bigint PRIMARY KEY AUTO_INCREMENT,
  `store_id` bigint NOT NULL,
  `member_id` bigint,
  `order_date` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP),
  `status` varchar(50) DEFAULT 'ORDERED'
);

CREATE TABLE `snack_order_item` (
  `order_id` bigint NOT NULL,
  `snack_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` int NOT NULL,
  `total_price` int NOT NULL,
  PRIMARY KEY (`order_id`, `snack_id`)
);

CREATE UNIQUE INDEX `review_like_review_member_uniq` ON `review_like` (`review_id`, `member_id`);

ALTER TABLE `booking_seat` COMMENT = 'PRIMARY KEY (booking_id, seat_id)';

ALTER TABLE `theater` ADD FOREIGN KEY (`city_region_id`) REFERENCES `city_region` (`city_region_id`);

ALTER TABLE `screen` ADD FOREIGN KEY (`theater_id`) REFERENCES `theater` (`theater_id`);

ALTER TABLE `screen_layout` ADD FOREIGN KEY (`screen_id`) REFERENCES `screen` (`screen_id`);

ALTER TABLE `seat` ADD FOREIGN KEY (`layout_id`) REFERENCES `screen_layout` (`layout_id`);

ALTER TABLE `seat` ADD FOREIGN KEY (`seat_type_id`) REFERENCES `seat_type` (`seat_type_id`);

ALTER TABLE `movie` ADD FOREIGN KEY (`age_rating_id`) REFERENCES `age_rating` (`age_rating_id`);

ALTER TABLE `movie` ADD FOREIGN KEY (`distributor_id`) REFERENCES `distributor` (`distributor_id`);

ALTER TABLE `movie_version` ADD FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`);

ALTER TABLE `movie_version` ADD FOREIGN KEY (`audio_lang_id`) REFERENCES `language` (`language_id`);

ALTER TABLE `movie_version` ADD FOREIGN KEY (`subtitle_id`) REFERENCES `subtitle` (`subtitle_id`);

ALTER TABLE `showtime` ADD FOREIGN KEY (`screen_id`) REFERENCES `screen` (`screen_id`);

ALTER TABLE `showtime` ADD FOREIGN KEY (`version_id`) REFERENCES `movie_version` (`version_id`);

ALTER TABLE `showtime` ADD FOREIGN KEY (`show_status_id`) REFERENCES `show_status` (`show_status_id`);

ALTER TABLE `price_rule_applies` ADD FOREIGN KEY (`rule_id`) REFERENCES `price_rule` (`rule_id`);

ALTER TABLE `price_rule_applies` ADD FOREIGN KEY (`theater_id`) REFERENCES `theater` (`theater_id`);

ALTER TABLE `price_rule_applies` ADD FOREIGN KEY (`screen_id`) REFERENCES `screen` (`screen_id`);

ALTER TABLE `price_rule_applies` ADD FOREIGN KEY (`seat_type_id`) REFERENCES `seat_type` (`seat_type_id`);

ALTER TABLE `price` ADD FOREIGN KEY (`show_id`) REFERENCES `showtime` (`show_id`);

ALTER TABLE `price` ADD FOREIGN KEY (`seat_type_id`) REFERENCES `seat_type` (`seat_type_id`);

ALTER TABLE `price` ADD FOREIGN KEY (`rule_apply_id`) REFERENCES `price_rule_applies` (`rule_apply_id`);

ALTER TABLE `maintenance_ticket` ADD FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`);

ALTER TABLE `member` ADD FOREIGN KEY (`tier_id`) REFERENCES `member_tier` (`tier_id`);

ALTER TABLE `hold_seat` ADD FOREIGN KEY (`show_id`) REFERENCES `showtime` (`show_id`);

ALTER TABLE `hold_seat` ADD FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`);

ALTER TABLE `hold_seat` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `booking` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `booking` ADD FOREIGN KEY (`show_id`) REFERENCES `showtime` (`show_id`);

ALTER TABLE `booking_seat` ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`);

ALTER TABLE `booking_seat` ADD FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`);

ALTER TABLE `booking_seat` ADD FOREIGN KEY (`show_id`) REFERENCES `showtime` (`show_id`);

ALTER TABLE `payment` ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`);

ALTER TABLE `payment` ADD FOREIGN KEY (`method_id`) REFERENCES `payment_method` (`method_id`);

ALTER TABLE `coupon` ADD FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`promotion_id`);

ALTER TABLE `coupon_redeem` ADD FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`);

ALTER TABLE `coupon_redeem` ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`);

ALTER TABLE `refund` ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`);

ALTER TABLE `refund` ADD FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`);

ALTER TABLE `ticket` ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`);

ALTER TABLE `notification` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `review` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `review` ADD FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`);

ALTER TABLE `review_like` ADD FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`);

ALTER TABLE `review_like` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `member_movie_preference` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `movie_similarity` ADD FOREIGN KEY (`movie_id_1`) REFERENCES `movie` (`movie_id`);

ALTER TABLE `movie_similarity` ADD FOREIGN KEY (`movie_id_2`) REFERENCES `movie` (`movie_id`);

ALTER TABLE `recommendation` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `recommendation` ADD FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`);

ALTER TABLE `member_role` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `member_login_history` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `member_block_list` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `booking_non_member` ADD FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`);

ALTER TABLE `booking_non_member` ADD FOREIGN KEY (`non_member_id`) REFERENCES `non_member` (`non_member_id`);

ALTER TABLE `snack_store` ADD FOREIGN KEY (`theater_id`) REFERENCES `theater` (`theater_id`);

ALTER TABLE `snack_order` ADD FOREIGN KEY (`store_id`) REFERENCES `snack_store` (`store_id`);

ALTER TABLE `snack_order` ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `snack_order_item` ADD FOREIGN KEY (`order_id`) REFERENCES `snack_order` (`order_id`);

ALTER TABLE `snack_order_item` ADD FOREIGN KEY (`snack_id`) REFERENCES `menu` (`snack_id`);
