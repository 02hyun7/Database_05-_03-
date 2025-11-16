import random
from datetime import datetime, timedelta

# 재현 가능하도록 시드 고정
random.seed(42)

# ====== 레코드 개수 설정 (필요하면 여기만 수정해서 전체 볼륨 조절) ======
AGE_RATING_COUNT = 5
CITY_REGION_COUNT = 20
DISTRIBUTOR_COUNT = 20
LANGUAGE_COUNT = 10
MEMBER_TIER_COUNT = 4
PAYMENT_METHOD_COUNT = 5
PRICE_RULE_COUNT = 10
PROMOTION_COUNT = 10
SUBTITLE_COUNT = 10
TAX_COUNT = 5
THEATER_COUNT = 15
SCREEN_COUNT = 30
SCREEN_LAYOUT_COUNT = 30
SEAT_TYPE_COUNT = 5
SEAT_COUNT = 600
SHOW_STATUS_COUNT = 4
MOVIE_COUNT = 80
MOVIE_VERSION_COUNT = 120
SHOWTIME_COUNT = 200
PRICE_RULE_APPLIES_COUNT = 30
PRICE_COUNT = 400
MAINTENANCE_TICKET_COUNT = 40
MEMBER_COUNT = 250
NON_MEMBER_COUNT = 150
SNACK_STORE_COUNT = 15
MENU_COUNT = 40
SNACK_ORDER_COUNT = 150
SNACK_ORDER_ITEM_COUNT = 250
HOLD_SEAT_COUNT = 60
BOOKING_COUNT = 250
BOOKING_NON_MEMBER_COUNT = 120
BOOKING_SEAT_COUNT = 400
PAYMENT_COUNT = 230
COUPON_COUNT = 80
COUPON_REDEEM_COUNT = 120
REFUND_COUNT = 40
TICKET_COUNT = 250
NOTIFICATION_COUNT = 250
REVIEW_COUNT = 200
REVIEW_LIKE_COUNT = 200
MEMBER_MOVIE_PREF_COUNT = 200
MOVIE_SIMILARITY_COUNT = 150
RECOMMENDATION_COUNT = 250
MEMBER_ROLE_COUNT = 250
MEMBER_LOGIN_HISTORY_COUNT = 300
MEMBER_BLOCK_LIST_COUNT = 30
AUDIT_LOG_COUNT = 150

BASE_DATE = datetime(2023, 1, 1)


# ====== 공통 유틸 함수 ======

def q(s: str) -> str:
    """SQL 문자열 리터럴용 quoting"""
    return "'" + s.replace("'", "''") + "'"


def rand_datetime(days_range: int = 365) -> str:
    dt = BASE_DATE + timedelta(
        days=random.randint(0, days_range),
        seconds=random.randint(0, 24 * 3600 - 1)
    )
    return dt.strftime("'%Y-%m-%d %H:%M:%S'")


def rand_date(days_range: int = 365) -> str:
    d = BASE_DATE.date() + timedelta(days=random.randint(0, days_range))
    return d.strftime("'%Y-%m-%d'")


def rand_time() -> str:
    h = random.randint(9, 23)
    m = random.randint(0, 59)
    s = random.randint(0, 59)
    return f"'{h:02d}:{m:02d}:{s:02d}'"


def rand_choice_or_none(options, prob_none=0.1):
    if random.random() < prob_none:
        return "NULL"
    return q(random.choice(options))


# ====== 테이블별 INSERT 생성 ======

def insert_age_rating():
    labels = [("ALL", "All Ages"),
              ("7", "7+"),
              ("12", "12+"),
              ("15", "15+"),
              ("19", "Adults Only")]
    print("-- age_rating")
    for i in range(1, AGE_RATING_COUNT + 1):
        code, label = labels[(i - 1) % len(labels)]
        print(
            "INSERT INTO `age_rating` (`age_rating_id`, `code`, `label`) "
            f"VALUES ({i}, {q(code)}, {q(label)});"
        )


def insert_city_region():
    print("-- city_region")
    for i in range(1, CITY_REGION_COUNT + 1):
        city = f"City{i}"
        district = f"District{i}"
        print(
            "INSERT INTO `city_region` (`city_region_id`, `city`, `district`) "
            f"VALUES ({i}, {q(city)}, {q(district)});"
        )


def insert_distributor():
    print("-- distributor")
    for i in range(1, DISTRIBUTOR_COUNT + 1):
        name = f"Distributor {i}"
        print(
            "INSERT INTO `distributor` (`distributor_id`, `name`) "
            f"VALUES ({i}, {q(name)});"
        )


def insert_language():
    print("-- language")
    codes = ["ko", "en", "ja", "zh", "fr", "de", "es", "it", "ru", "pt"]
    for i in range(1, LANGUAGE_COUNT + 1):
        code = codes[(i - 1) % len(codes)]
        label = code.upper()
        print(
            "INSERT INTO `language` (`language_id`, `code`, `label`) "
            f"VALUES ({i}, {q(code)}, {q(label)});"
        )


def insert_member_tier():
    print("-- member_tier")
    tiers = [("BRONZE", 0), ("SILVER", 100000), ("GOLD", 300000), ("PLATINUM", 600000)]
    for i in range(1, MEMBER_TIER_COUNT + 1):
        code, min_spend = tiers[(i - 1) % len(tiers)]
        print(
            "INSERT INTO `member_tier` (`tier_id`, `code`, `min_spend`) "
            f"VALUES ({i}, {q(code)}, {min_spend});"
        )


def insert_payment_method():
    print("-- payment_method")
    methods = ["CARD", "CASH", "POINT", "COUPON", "MOBILE"]
    for i in range(1, PAYMENT_METHOD_COUNT + 1):
        code = methods[(i - 1) % len(methods)]
        label = f"{code} Payment"
        print(
            "INSERT INTO `payment_method` (`method_id`, `code`, `label`) "
            f"VALUES ({i}, {q(code)}, {q(label)});"
        )


def insert_price_rule():
    print("-- price_rule")
    rule_types = ["DISCOUNT", "SURCHARGE"]
    amount_types = ["FIXED", "PERCENT"]
    for i in range(1, PRICE_RULE_COUNT + 1):
        name = f"Rule {i}"
        rtype = random.choice(rule_types)
        atype = random.choice(amount_types)
        if atype == "PERCENT":
            amount = round(random.uniform(5, 30), 2)
        else:
            amount = round(random.uniform(1000, 5000), 2)
        active_from = rand_datetime(200)
        active_to = rand_datetime(400)
        print(
            "INSERT INTO `price_rule` "
            "(`rule_id`, `name`, `rule_type`, `priority`, `amount_type`, "
            "`amount_value`, `active_from`, `active_to`) VALUES "
            f"({i}, {q(name)}, {q(rtype)}, {100 + i}, {q(atype)}, "
            f"{amount}, {active_from}, {active_to});"
        )


def insert_promotion():
    print("-- promotion")
    for i in range(1, PROMOTION_COUNT + 1):
        name = f"Promo {i}"
        desc = f"Promotion description {i}"
        active_from = rand_datetime(300)
        active_to = rand_datetime(600)
        print(
            "INSERT INTO `promotion` "
            "(`promotion_id`, `name`, `description`, `active_from`, `active_to`) "
            f"VALUES ({i}, {q(name)}, {q(desc)}, {active_from}, {active_to});"
        )


def insert_subtitle():
    print("-- subtitle")
    codes = ["ko", "en", "none", "jp", "zh", "fr", "de", "es", "it", "ru"]
    for i in range(1, SUBTITLE_COUNT + 1):
        code = f"sub_{codes[(i - 1) % len(codes)]}"
        label = f"Subtitle {codes[(i - 1) % len(codes)].upper()}"
        print(
            "INSERT INTO `subtitle` (`subtitle_id`, `code`, `label`) "
            f"VALUES ({i}, {q(code)}, {q(label)});"
        )


def insert_tax():
    print("-- tax")
    for i in range(1, TAX_COUNT + 1):
        name = f"Tax {i}"
        rate = round(random.uniform(5, 15), 2)
        effective_from = rand_datetime(500)
        effective_to = rand_datetime(800)
        print(
            "INSERT INTO `tax` "
            "(`tax_id`, `name`, `rate_pct`, `effective_from`, `effective_to`) "
            f"VALUES ({i}, {q(name)}, {rate}, {effective_from}, {effective_to});"
        )


def insert_theater():
    print("-- theater")
    for i in range(1, THEATER_COUNT + 1):
        city_region_id = random.randint(1, CITY_REGION_COUNT)
        name = f"Theater {i}"
        address = f"Address {i}"
        print(
            "INSERT INTO `theater` "
            "(`theater_id`, `city_region_id`, `name`, `address`) "
            f"VALUES ({i}, {city_region_id}, {q(name)}, {q(address)});"
        )


def insert_screen():
    print("-- screen")
    screen_id = 1
    for t in range(1, THEATER_COUNT + 1):
        # 각 극장에 스크린 2개 정도씩
        for local_idx in range(2):
            if screen_id > SCREEN_COUNT:
                return
            name = f"Screen {screen_id}"
            layout_version = 1
            print(
                "INSERT INTO `screen` "
                "(`screen_id`, `theater_id`, `name`, `layout_version`) "
                f"VALUES ({screen_id}, {t}, {q(name)}, {layout_version});"
            )
            screen_id += 1


def insert_screen_layout():
    print("-- screen_layout")
    layout_id = 1
    for s in range(1, SCREEN_COUNT + 1):
        if layout_id > SCREEN_LAYOUT_COUNT:
            break
        version = 1
        created_at = rand_datetime(400)
        print(
            "INSERT INTO `screen_layout` "
            "(`layout_id`, `screen_id`, `version`, `created_at`) "
            f"VALUES ({layout_id}, {s}, {version}, {created_at});"
        )
        layout_id += 1
    # 남는 layout_id가 있으면 랜덤 screen에 추가
    while layout_id <= SCREEN_LAYOUT_COUNT:
        s = random.randint(1, SCREEN_COUNT)
        version = random.randint(1, 3)
        created_at = rand_datetime(400)
        print(
            "INSERT INTO `screen_layout` "
            "(`layout_id`, `screen_id`, `version`, `created_at`) "
            f"VALUES ({layout_id}, {s}, {version}, {created_at});"
        )
        layout_id += 1


def insert_seat_type():
    print("-- seat_type")
    types = [
        ("NORMAL", "Normal Seat", 0),
        ("PREMIUM", "Premium Seat", 3000),
        ("COUPLE", "Couple Seat", 5000),
        ("4DX", "4DX Seat", 7000),
        ("IMAX", "IMAX Seat", 8000),
    ]
    for i in range(1, SEAT_TYPE_COUNT + 1):
        code, label, delta = types[(i - 1) % len(types)]
        print(
            "INSERT INTO `seat_type` "
            "(`seat_type_id`, `code`, `label`, `price_delta`) "
            f"VALUES ({i}, {q(code)}, {q(label)}, {delta});"
        )


def insert_seat():
    print("-- seat")
    # layout별 20좌석씩 (4행 x 5열) => 30 * 20 = 600
    seat_id = 1
    row_labels = ["A", "B", "C", "D"]
    for layout_id in range(1, SCREEN_LAYOUT_COUNT + 1):
        for r in row_labels:
            for c in range(1, 6):
                if seat_id > SEAT_COUNT:
                    return
                seat_type_id = random.randint(1, SEAT_TYPE_COUNT)
                print(
                    "INSERT INTO `seat` "
                    "(`seat_id`, `layout_id`, `row_label`, `col_number`, `seat_type_id`) "
                    f"VALUES ({seat_id}, {layout_id}, {q(r)}, {c}, {seat_type_id});"
                )
                seat_id += 1


def insert_show_status():
    print("-- show_status")
    statuses = [("SCHEDULED", "Scheduled"),
                ("OPEN", "Open for booking"),
                ("CLOSED", "Closed"),
                ("CANCELLED", "Cancelled")]
    for i in range(1, SHOW_STATUS_COUNT + 1):
        code, label = statuses[(i - 1) % len(statuses)]
        print(
            "INSERT INTO `show_status` (`show_status_id`, `code`, `label`) "
            f"VALUES ({i}, {q(code)}, {q(label)});"
        )


def insert_movie():
    print("-- movie")
    for i in range(1, MOVIE_COUNT + 1):
        title = f"Movie {i}"
        duration = random.randint(80, 150)
        released_on = rand_date(800)
        age_rating_id = random.randint(1, AGE_RATING_COUNT)
        distributor_id = random.randint(1, DISTRIBUTOR_COUNT)
        print(
            "INSERT INTO `movie` "
            "(`movie_id`, `title`, `duration_min`, `released_on`, "
            "`age_rating_id`, `distributor_id`) "
            f"VALUES ({i}, {q(title)}, {duration}, {released_on}, "
            f"{age_rating_id}, {distributor_id});"
        )


def insert_movie_version():
    print("-- movie_version")
    formats = ["2D", "3D", "IMAX", "4DX"]
    version_id = 1
    # 각 영화당 1~2개 버전 만들어서 총 MOVIE_VERSION_COUNT 근처로
    while version_id <= MOVIE_VERSION_COUNT:
        for m in range(1, MOVIE_COUNT + 1):
            if version_id > MOVIE_VERSION_COUNT:
                break
            fmt = random.choice(formats)
            audio_lang_id = random.randint(1, LANGUAGE_COUNT)
            subtitle_id = random.randint(1, SUBTITLE_COUNT)
            print(
                "INSERT INTO `movie_version` "
                "(`version_id`, `movie_id`, `format`, `audio_lang_id`, `subtitle_id`) "
                f"VALUES ({version_id}, {m}, {q(fmt)}, {audio_lang_id}, {subtitle_id});"
            )
            version_id += 1


def insert_showtime():
    print("-- showtime")
    for show_id in range(1, SHOWTIME_COUNT + 1):
        screen_id = random.randint(1, SCREEN_COUNT)
        version_id = random.randint(1, MOVIE_VERSION_COUNT)
        starts_at_dt = BASE_DATE + timedelta(
            days=random.randint(0, 60),
            hours=random.randint(9, 22),
            minutes=random.randint(0, 59)
        )
        ends_at_dt = starts_at_dt + timedelta(minutes=120)
        starts_at = starts_at_dt.strftime("'%Y-%m-%d %H:%M:%S'")
        ends_at = ends_at_dt.strftime("'%Y-%m-%d %H:%M:%S'")
        show_status_id = random.randint(1, SHOW_STATUS_COUNT)
        print(
            "INSERT INTO `showtime` "
            "(`show_id`, `screen_id`, `version_id`, `starts_at`, `ends_at`, `show_status_id`) "
            f"VALUES ({show_id}, {screen_id}, {version_id}, "
            f"{starts_at}, {ends_at}, {show_status_id});"
        )


def insert_price_rule_applies():
    print("-- price_rule_applies")
    for i in range(1, PRICE_RULE_APPLIES_COUNT + 1):
        rule_id = random.randint(1, PRICE_RULE_COUNT)
        theater_id = random.randint(1, THEATER_COUNT)
        screen_id = random.randint(1, SCREEN_COUNT)
        seat_type_id = random.randint(1, SEAT_TYPE_COUNT)
        day_of_week = random.randint(0, 6)
        time_from = "'09:00:00'"
        time_to = "'23:59:59'"
        print(
            "INSERT INTO `price_rule_applies` "
            "(`rule_apply_id`, `rule_id`, `theater_id`, `screen_id`, "
            "`seat_type_id`, `day_of_week`, `time_from`, `time_to`) "
            f"VALUES ({i}, {rule_id}, {theater_id}, {screen_id}, "
            f"{seat_type_id}, {day_of_week}, {time_from}, {time_to});"
        )


def insert_price():
    print("-- price")
    # 각 상영당 2개 seat_type 정도 => SHOWTIME_COUNT * 2 = 400
    price_id = 1
    for show_id in range(1, SHOWTIME_COUNT + 1):
        if price_id > PRICE_COUNT:
            break
        seat_types = random.sample(range(1, SEAT_TYPE_COUNT + 1), 2)
        for st in seat_types:
            if price_id > PRICE_COUNT:
                break
            default_price = random.randint(8000, 20000)
            changed_price = "NULL"
            rule_apply_id = random.randint(1, PRICE_RULE_APPLIES_COUNT)
            print(
                "INSERT INTO `price` "
                "(`price_id`, `show_id`, `seat_type_id`, `default_price`, "
                "`changed_price`, `rule_apply_id`) "
                f"VALUES ({price_id}, {show_id}, {st}, {default_price}, "
                f"{changed_price}, {rule_apply_id});"
            )
            price_id += 1


def insert_member():
    print("-- member")
    for i in range(1, MEMBER_COUNT + 1):
        email = f"user{i}@example.com"
        name = f"Member {i}"
        tier_id = random.randint(1, MEMBER_TIER_COUNT)
        created_at = rand_datetime(700)
        print(
            "INSERT INTO `member` "
            "(`member_id`, `email`, `name`, `tier_id`, `created_at`) "
            f"VALUES ({i}, {q(email)}, {q(name)}, {tier_id}, {created_at});"
        )


def insert_non_member():
    print("-- non_member")
    for i in range(1, NON_MEMBER_COUNT + 1):
        name = f"Guest {i}"
        phone = f"010-{random.randint(1000,9999)}-{random.randint(1000,9999)}"
        email = f"guest{i}@example.com"
        created_at = rand_datetime(700)
        print(
            "INSERT INTO `non_member` "
            "(`non_member_id`, `name`, `phone`, `email`, `created_at`) "
            f"VALUES ({i}, {q(name)}, {q(phone)}, {q(email)}, {created_at});"
        )


def insert_snack_store():
    print("-- snack_store")
    for i in range(1, SNACK_STORE_COUNT + 1):
        theater_id = random.randint(1, THEATER_COUNT)
        store_name = f"Snack Store {i}"
        open_time = "'09:00:00'"
        close_time = "'23:00:00'"
        print(
            "INSERT INTO `snack_store` "
            "(`store_id`, `theater_id`, `store_name`, `open_time`, `close_time`) "
            f"VALUES ({i}, {theater_id}, {q(store_name)}, {open_time}, {close_time});"
        )


def insert_menu():
    print("-- menu")
    base_snacks = ["Popcorn", "Cola", "Nachos", "Hotdog", "Coffee", "Water", "Juice"]
    for i in range(1, MENU_COUNT + 1):
        snack_name = f"{random.choice(base_snacks)} {i}"
        price = random.randint(3000, 12000)
        snack_type = random.choice(["FOOD", "DRINK", "SET"])
        print(
            "INSERT INTO `menu` "
            "(`snack_id`, `snack_name`, `snack_price`, `snack_type`) "
            f"VALUES ({i}, {q(snack_name)}, {price}, {q(snack_type)});"
        )


def insert_snack_order():
    print("-- snack_order")
    for i in range(1, SNACK_ORDER_COUNT + 1):
        store_id = random.randint(1, SNACK_STORE_COUNT)
        if random.random() < 0.7:
            member_id = random.randint(1, MEMBER_COUNT)
        else:
            member_id = "NULL"
        order_date = rand_datetime(200)
        status = random.choice(["ORDERED", "PREPARING", "READY", "COMPLETED", "CANCELLED"])
        member_id_val = member_id if member_id == "NULL" else str(member_id)
        print(
            "INSERT INTO `snack_order` "
            "(`order_id`, `store_id`, `member_id`, `order_date`, `status`) "
            f"VALUES ({i}, {store_id}, {member_id_val}, {order_date}, {q(status)});"
        )


def insert_snack_order_item():
    print("-- snack_order_item")
    used = set()
    count = 0
    while count < SNACK_ORDER_ITEM_COUNT:
        order_id = random.randint(1, SNACK_ORDER_COUNT)
        snack_id = random.randint(1, MENU_COUNT)
        key = (order_id, snack_id)
        if key in used:
            continue
        used.add(key)
        quantity = random.randint(1, 3)
        unit_price = random.randint(3000, 12000)
        total_price = unit_price * quantity
        print(
            "INSERT INTO `snack_order_item` "
            "(`order_id`, `snack_id`, `quantity`, `unit_price`, `total_price`) "
            f"VALUES ({order_id}, {snack_id}, {quantity}, {unit_price}, {total_price});"
        )
        count += 1


def insert_maintenance_ticket():
    print("-- maintenance_ticket")
    for i in range(1, MAINTENANCE_TICKET_COUNT + 1):
        seat_id = random.randint(1, SEAT_COUNT)
        opened_at = rand_datetime(400)
        if random.random() < 0.6:
            closed_at = rand_datetime(200)
            status = "CLOSED"
        else:
            closed_at = "NULL"
            status = "OPEN"
        closed_at_val = closed_at if closed_at == "NULL" else closed_at
        print(
            "INSERT INTO `maintenance_ticket` "
            "(`ticket_id`, `seat_id`, `opened_at`, `closed_at`, `status`) "
            f"VALUES ({i}, {seat_id}, {opened_at}, {closed_at_val}, {q(status)});"
        )


def insert_hold_seat():
    print("-- hold_seat")
    for i in range(1, HOLD_SEAT_COUNT + 1):
        show_id = random.randint(1, SHOWTIME_COUNT)
        seat_id = random.randint(1, SEAT_COUNT)
        member_id = random.randint(1, MEMBER_COUNT)
        expires_at = rand_datetime(60)
        print(
            "INSERT INTO `hold_seat` "
            "(`hold_id`, `show_id`, `seat_id`, `member_id`, `expires_at`) "
            f"VALUES ({i}, {show_id}, {seat_id}, {member_id}, {expires_at});"
        )


# booking에서 show_id를 다시 쓰지는 않지만, FK만 맞으면 되므로 간단히 랜덤 사용
def insert_booking():
    print("-- booking")
    for i in range(1, BOOKING_COUNT + 1):
        member_id = random.randint(1, MEMBER_COUNT)
        show_id = random.randint(1, SHOWTIME_COUNT)
        status = random.choice(["PENDING", "CONFIRMED", "CANCELLED"])
        total_amount = random.randint(10000, 80000)
        created_at = rand_datetime(200)
        updated_at = rand_datetime(200)
        print(
            "INSERT INTO `booking` "
            "(`booking_id`, `member_id`, `show_id`, `status`, `total_amount`, "
            "`created_at`, `updated_at`) "
            f"VALUES ({i}, {member_id}, {show_id}, {q(status)}, "
            f"{total_amount}, {created_at}, {updated_at});"
        )


def insert_booking_non_member():
    print("-- booking_non_member")
    # 일부 booking만 non_member 연결
    bookings = random.sample(range(1, BOOKING_COUNT + 1),
                             min(BOOKING_NON_MEMBER_COUNT, BOOKING_COUNT))
    for i, booking_id in enumerate(bookings, start=1):
        if i > BOOKING_NON_MEMBER_COUNT:
            break
        non_member_id = random.randint(1, NON_MEMBER_COUNT)
        print(
            "INSERT INTO `booking_non_member` "
            "(`booking_id`, `non_member_id`) "
            f"VALUES ({booking_id}, {non_member_id});"
        )


def insert_booking_seat():
    print("-- booking_seat")
    used = set()   # ← (booking_id, seat_id) 중복 방지
    count = 0
    while count < BOOKING_SEAT_COUNT:
        booking_id = random.randint(1, BOOKING_COUNT)
        seat_id = random.randint(1, SEAT_COUNT)

        key = (booking_id, seat_id)
        if key in used:
            continue  # 이미 존재 → 다시 뽑기

        used.add(key)

        show_id = random.randint(1, SHOWTIME_COUNT)
        price = random.randint(8000, 20000)

        print(
            "INSERT INTO `booking_seat` "
            "(`booking_id`, `seat_id`, `show_id`, `price`) "
            f"VALUES ({booking_id}, {seat_id}, {show_id}, {price});"
        )
        count += 1



def insert_payment():
    print("-- payment")
    for i in range(1, PAYMENT_COUNT + 1):
        booking_id = random.randint(1, BOOKING_COUNT)
        method_id = random.randint(1, PAYMENT_METHOD_COUNT)
        amount = random.randint(8000, 80000)
        status = random.choice(["APPROVED", "DECLINED", "PENDING"])
        if status == "APPROVED":
            approved_at = rand_datetime(200)
        else:
            approved_at = "NULL"
        approved_at_val = approved_at if approved_at == "NULL" else approved_at
        print(
            "INSERT INTO `payment` "
            "(`payment_id`, `booking_id`, `method_id`, `amount`, `status`, `approved_at`) "
            f"VALUES ({i}, {booking_id}, {method_id}, {amount}, "
            f"{q(status)}, {approved_at_val});"
        )


def insert_coupon():
    print("-- coupon")
    for i in range(1, COUPON_COUNT + 1):
        promotion_id = random.randint(1, PROMOTION_COUNT)
        code = f"CPN{i:04d}"
        max_uses = random.randint(1, 5)
        remaining = random.randint(0, max_uses)
        discount_type = random.choice(["PERCENT", "FIXED"])
        if discount_type == "PERCENT":
            discount_value = round(random.uniform(5, 30), 2)
        else:
            discount_value = random.randint(1000, 5000)
        valid_from = rand_datetime(200)
        valid_to = rand_datetime(600)
        print(
            "INSERT INTO `coupon` "
            "(`coupon_id`, `promotion_id`, `code`, `max_uses`, `remaining`, "
            "`discount_type`, `discount_value`, `valid_from`, `valid_to`) "
            f"VALUES ({i}, {promotion_id}, {q(code)}, {max_uses}, {remaining}, "
            f"{q(discount_type)}, {discount_value}, {valid_from}, {valid_to});"
        )


def insert_coupon_redeem():
    print("-- coupon_redeem")
    for i in range(1, COUPON_REDEEM_COUNT + 1):
        coupon_id = random.randint(1, COUPON_COUNT)
        booking_id = random.randint(1, BOOKING_COUNT)
        redeemed_at = rand_datetime(200)
        print(
            "INSERT INTO `coupon_redeem` "
            "(`redeem_id`, `coupon_id`, `booking_id`, `redeemed_at`) "
            f"VALUES ({i}, {coupon_id}, {booking_id}, {redeemed_at});"
        )


def insert_refund():
    print("-- refund")
    for i in range(1, REFUND_COUNT + 1):
        booking_id = random.randint(1, BOOKING_COUNT)
        payment_id = random.randint(1, PAYMENT_COUNT)
        amount = random.randint(5000, 30000)
        reason = random.choice(["Customer Request", "System Error", "Show Cancelled"])
        created_at = rand_datetime(200)
        print(
            "INSERT INTO `refund` "
            "(`refund_id`, `booking_id`, `payment_id`, `amount`, `reason`, `created_at`) "
            f"VALUES ({i}, {booking_id}, {payment_id}, {amount}, {q(reason)}, {created_at});"
        )


def insert_ticket():
    print("-- ticket")
    for i in range(1, TICKET_COUNT + 1):
        booking_id = i  # 앞부분 booking과 1:1 대응
        issued_at = rand_datetime(200)
        qr_code = f"QR{i:06d}"
        print(
            "INSERT INTO `ticket` "
            "(`ticket_id`, `booking_id`, `issued_at`, `qr_code`) "
            f"VALUES ({i}, {booking_id}, {issued_at}, {q(qr_code)});"
        )


def insert_notification():
    print("-- notification")
    for i in range(1, NOTIFICATION_COUNT + 1):
        member_id = random.randint(1, MEMBER_COUNT)
        title = f"Notification {i}"
        body = f"Message body for notification {i}"
        sent_at = rand_datetime(200)
        channel = random.choice(["EMAIL", "SMS", "PUSH"])
        print(
            "INSERT INTO `notification` "
            "(`notification_id`, `member_id`, `title`, `body`, `sent_at`, `channel`) "
            f"VALUES ({i}, {member_id}, {q(title)}, {q(body)}, {sent_at}, {q(channel)});"
        )


def insert_review():
    print("-- review")
    for i in range(1, REVIEW_COUNT + 1):
        member_id = random.randint(1, MEMBER_COUNT)
        movie_id = random.randint(1, MOVIE_COUNT)
        rating = random.randint(1, 5)
        content = f"Review content {i}"
        created_at = rand_datetime(400)
        print(
            "INSERT INTO `review` "
            "(`review_id`, `member_id`, `movie_id`, `rating`, `content`, `created_at`) "
            f"VALUES ({i}, {member_id}, {movie_id}, {rating}, {q(content)}, {created_at});"
        )


def insert_review_like():
    print("-- review_like")
    used = set()
    count = 0
    while count < REVIEW_LIKE_COUNT:
        review_id = random.randint(1, REVIEW_COUNT)
        member_id = random.randint(1, MEMBER_COUNT)
        key = (review_id, member_id)
        if key in used:
            continue
        used.add(key)
        created_at = rand_datetime(200)
        print(
            "INSERT INTO `review_like` "
            "(`review_like_id`, `review_id`, `member_id`, `created_at`) "
            f"VALUES ({count + 1}, {review_id}, {member_id}, {created_at});"
        )
        count += 1


def insert_member_movie_preference():
    print("-- member_movie_preference")
    for i in range(1, MEMBER_MOVIE_PREF_COUNT + 1):
        member_id = random.randint(1, MEMBER_COUNT)
        genre_id = random.randint(1, 10)  # 장르 테이블은 없으니 그냥 숫자
        score = round(random.uniform(0, 1), 2)
        created_at = rand_datetime(400)
        print(
            "INSERT INTO `member_movie_preference` "
            "(`preference_id`, `member_id`, `genre_id`, `preference_score`, `created_at`) "
            f"VALUES ({i}, {member_id}, {genre_id}, {score}, {created_at});"
        )


def insert_movie_similarity():
    print("-- movie_similarity")
    used = set()
    count = 0
    while count < MOVIE_SIMILARITY_COUNT:
        m1 = random.randint(1, MOVIE_COUNT)
        m2 = random.randint(1, MOVIE_COUNT)
        if m1 == m2:
            continue
        key = (m1, m2)
        if key in used:
            continue
        used.add(key)
        score = round(random.uniform(0, 1), 2)
        print(
            "INSERT INTO `movie_similarity` "
            "(`movie_id_1`, `movie_id_2`, `similarity_score`) "
            f"VALUES ({m1}, {m2}, {score});"
        )
        count += 1


def insert_recommendation():
    print("-- recommendation")
    for i in range(1, RECOMMENDATION_COUNT + 1):
        member_id = random.randint(1, MEMBER_COUNT)
        movie_id = random.randint(1, MOVIE_COUNT)
        rank_score = round(random.uniform(0, 1), 2)
        generated_at = rand_datetime(200)
        print(
            "INSERT INTO `recommendation` "
            "(`recommendation_id`, `member_id`, `movie_id`, `rank_score`, `generated_at`) "
            f"VALUES ({i}, {member_id}, {movie_id}, {rank_score}, {generated_at});"
        )


def insert_member_role():
    print("-- member_role")
    # 간단하게 앞쪽 멤버들에게 USER 역할 부여
    for i in range(1, MEMBER_ROLE_COUNT + 1):
        member_id = i if i <= MEMBER_COUNT else random.randint(1, MEMBER_COUNT)
        role = "USER"
        print(
            "INSERT INTO `member_role` "
            "(`member_id`, `role`) "
            f"VALUES ({member_id}, {q(role)});"
        )


def insert_member_login_history():
    print("-- member_login_history")
    for i in range(1, MEMBER_LOGIN_HISTORY_COUNT + 1):
        member_id = random.randint(1, MEMBER_COUNT)
        login_time = rand_datetime(400)
        ip_addr = f"192.168.{random.randint(0,255)}.{random.randint(1,254)}"
        device_info = random.choice(["Android", "iOS", "Web", "Tablet"])
        print(
            "INSERT INTO `member_login_history` "
            "(`login_id`, `member_id`, `login_time`, `ip_addr`, `device_info`) "
            f"VALUES ({i}, {member_id}, {login_time}, {q(ip_addr)}, {q(device_info)});"
        )


def insert_member_block_list():
    print("-- member_block_list")
    blocked_members = random.sample(range(1, MEMBER_COUNT + 1),
                                    min(MEMBER_BLOCK_LIST_COUNT, MEMBER_COUNT))
    for i, member_id in enumerate(blocked_members, start=1):
        if i > MEMBER_BLOCK_LIST_COUNT:
            break
        reason = random.choice(["Abuse", "Spam", "Chargeback"])
        blocked_at = rand_datetime(400)
        print(
            "INSERT INTO `member_block_list` "
            "(`block_id`, `member_id`, `reason`, `blocked_at`) "
            f"VALUES ({i}, {member_id}, {q(reason)}, {blocked_at});"
        )


def insert_audit_log():
    print("-- audit_log")
    actor_types = ["SYSTEM", "MEMBER"]
    actions = ["INSERT", "UPDATE", "DELETE", "LOGIN", "LOGOUT"]
    target_tables = [
        "booking", "member", "ticket", "payment", "review",
        "snack_order", "movie", "showtime"
    ]
    for i in range(1, AUDIT_LOG_COUNT + 1):
        actor_type = random.choice(actor_types)
        if actor_type == "MEMBER":
            actor_id = random.randint(1, MEMBER_COUNT)
        else:
            actor_id = "NULL"
        action = random.choice(actions)
        target_table = random.choice(target_tables)
        target_id = random.randint(1, 1000)
        created_at = rand_datetime(400)
        detail = q('{"info":"dummy"}')
        actor_id_val = actor_id if actor_id == "NULL" else str(actor_id)
        print(
            "INSERT INTO `audit_log` "
            "(`log_id`, `actor_type`, `actor_id`, `action`, `target_table`, "
            "`target_id`, `created_at`, `detail`) "
            f"VALUES ({i}, {q(actor_type)}, {actor_id_val}, {q(action)}, "
            f"{q(target_table)}, {target_id}, {created_at}, {detail});"
        )


# ====== 메인 실행 ======

def main():
    print("SET FOREIGN_KEY_CHECKS = 0;")

    insert_age_rating()
    insert_city_region()
    insert_distributor()
    insert_language()
    insert_member_tier()
    insert_payment_method()
    insert_price_rule()
    insert_promotion()
    insert_subtitle()
    insert_tax()
    insert_theater()
    insert_screen()
    insert_screen_layout()
    insert_seat_type()
    insert_seat()
    insert_show_status()
    insert_movie()
    insert_movie_version()
    insert_showtime()
    insert_price_rule_applies()
    insert_price()
    insert_member()
    insert_non_member()
    insert_snack_store()
    insert_menu()
    insert_snack_order()
    insert_snack_order_item()
    insert_maintenance_ticket()
    insert_hold_seat()
    insert_booking()
    insert_booking_non_member()
    insert_booking_seat()
    insert_payment()
    insert_coupon()
    insert_coupon_redeem()
    insert_refund()
    insert_ticket()
    insert_notification()
    insert_review()
    insert_review_like()
    insert_member_movie_preference()
    insert_movie_similarity()
    insert_recommendation()
    insert_member_role()
    insert_member_login_history()
    insert_member_block_list()
    insert_audit_log()

    print("SET FOREIGN_KEY_CHECKS = 1;")


if __name__ == "__main__":
    main()
