package nl.debijenkorf.snowplow.streams

import java.security.PrivateKey

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.alpakka.googlecloud.pubsub.{AcknowledgeRequest, ReceivedMessage}
import akka.stream.scaladsl.{Sink, Source}
import akka.{Done, NotUsed}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

case class PubSub(projectId: String,
                  clientEmail: String,
                  primaryKey: PrivateKey,
                  subscriptionName: String)
                 (implicit val actorSystem: ActorSystem,
                  implicit val mat: Materializer) extends LazyLogging {

  private val apiKey: String = "" // not used anymore

  def subscribe(): Source[ReceivedMessage, NotUsed] =
    GooglePubSub.subscribe(projectId, apiKey, clientEmail, primaryKey, subscriptionName)

  def acknowledge(): Sink[AcknowledgeRequest, Future[Done]] = {
    GooglePubSub.acknowledge(projectId, apiKey, clientEmail, primaryKey, subscriptionName)
  }
}
