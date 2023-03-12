package com.prabathj.bookinggw;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prabathj.hotelavailabilitysrv.model.AvailableRooms;
import com.prabathj.hotelavailabilitysrv.reqhandler.AvailabilityReqHandler;
@Disabled
@DisplayName("Test booking search by day")
public class BookingSearchByDayTest {
    private static final String BASE_URI = "http://localhost:5030/api/booking";
    private static final String APPLICATION_JSON = "application/json";
    
	@Test
	@DisplayName("booking search")
	public void bookingSearch() {
	
		AvailableRooms availability= given().accept(APPLICATION_JSON).param("date", LocalDate.now().toString())
			.when().get(BASE_URI)
			.then()
			.assertThat()
			.statusCode(200).extract().as(AvailableRooms.class);
	
		assertThat(availability.getRooms().size()).isLessThan(AvailabilityReqHandler.NO_ROOMS);
		
		List<Integer> rooms = availability.getRooms();

		for (Integer room : rooms) {
			assertThat(room).isLessThan(AvailabilityReqHandler.NO_ROOMS);
			System.out.println(room);
		}
		
	}    
}
