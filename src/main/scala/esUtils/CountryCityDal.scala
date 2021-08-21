package esUtils

import objects.{City, Country}

import scala.concurrent.Future

trait CountryCityDal {
  val indexName = "countries.and.cities"

  def loadAllCities(): Future[List[City]]

  def ingestCountries(countries: List[Country]): Future[List[Boolean]]

  def ingestCities(cities: List[City]): Future[List[Boolean]]

}
