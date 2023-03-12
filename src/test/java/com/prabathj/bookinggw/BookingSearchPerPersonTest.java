package com.prabathj.bookinggw;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prabathj.hotelavailabilitysrv.model.BookingInfo;
import com.prabathj.hotelavailabilitysrv.model.Bookings;
import com.prabathj.hotelavailabilitysrv.reqhandler.AvailabilityReqHandler;
@Disabled
@DisplayName("Test booking search per person")
public class BookingSearchPerPersonTest {
    private static final String BASE_URI = "http://localhost:5030/api/booking";
    private static final String APPLICATION_JSON = "application/json";
    
	@Test
	@DisplayName("Search request")
	public void bookingSearch() {
	
		Bookings bookedData= given().accept(APPLICATION_JSON).param("name", "mike")
			.when().get(BASE_URI)
			.then()
			.assertThat()
			.statusCode(200).extract().as(Bookings.class);
		
		List<BookingInfo> bookings = bookedData.getBookings();

		for (BookingInfo data : bookings) {
			assertThat(data.getRoom()).isLessThan(AvailabilityReqHandler.NO_ROOMS);
			System.out.println(data.toString());
		}
		
	}       
}
