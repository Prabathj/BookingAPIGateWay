package com.prabathj.bookinggw;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.prabathj.hotelavailabilitysrv.model.AvailableRooms;
import com.prabathj.hotelavailabilitysrv.model.BookingInfo;
import com.prabathj.hotelavailabilitysrv.model.Bookings;
import com.prabathj.hotelavailabilitysrv.reqhandler.AvailabilityReqHandler;

@Disabled
@DisplayName("Test Complete process make booking, search by data and search by user")
@TestMethodOrder(OrderAnnotation.class)
public class BookingCompleteProcessMannualServerStartTest {
	
	
    private static final String BASE_URI = "http://localhost:5030/api/booking";
    private static final String APPLICATION_JSON = "application/json";

    private static TestDataSetConfiguration sampleData;
    
    @BeforeAll
    public static void startup() {

    	sampleData=new TestDataSetConfiguration();
    }
	
	@Test
	@DisplayName("booking request")
	@Order(1)
	public void processBookings() {

		for (BookingInfo bookingInfo : sampleData.loadData()) {
			this.bookingRequest(bookingInfo);
		}
		
	}
	
	
	public void bookingRequest(BookingInfo mokeBooking) {
		

		given().contentType(APPLICATION_JSON)
			.body(mokeBooking)
			.when().post(BASE_URI)
			.then()
			.assertThat()
			.statusCode(201)
			.body("name",equalTo(mokeBooking.getName()))
			.body("room",equalTo(mokeBooking.getRoom()))
			.body("bookingDate",equalTo(mokeBooking.getBookingDate().toString()));		

	}
	
	
	
	@Test
	@DisplayName("Search by date request")
	@Order(2)
	public void bookingSearchByDate() {
	
		AvailableRooms availability= given().accept(APPLICATION_JSON).param("date", LocalDate.now().toString())
			.when().get(BASE_URI)
			.then()
			.assertThat()
			.statusCode(200).extract().as(AvailableRooms.class);
	
		assertThat(availability.getRooms().size()).isLessThan(AvailabilityReqHandler.NO_ROOMS);
		
		List<Integer> rooms = availability.getRooms();
		List<Integer> expected = sampleData.getAvailableRoomsListOfDateNow();
		
		assertEquals(expected,rooms);
		
	} 	
	
	
	@Test
	@DisplayName("Search by name request")
	@Order(3)
	public void bookingSearch() {
	
		
		Bookings bookedData= given().accept(APPLICATION_JSON).param("name", sampleData.getName())
				.when().get(BASE_URI)
				.then()
				.assertThat()
				.statusCode(200).extract().as(Bookings.class);
	
		List<BookingInfo> bookings = bookedData.getBookings();
		List<BookingInfo> expected = sampleData.getBookingsPerUser(sampleData.loadData(), sampleData.getName());
		
		assertThat(expected).hasSameElementsAs(bookings);

	}  	
	
	
	
	
}
